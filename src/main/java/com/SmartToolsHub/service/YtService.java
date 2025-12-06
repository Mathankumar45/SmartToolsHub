package com.SmartToolsHub.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class YtService {

    private final Map<String, JobStatus> jobs = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final Path downloadDir = Paths.get("downloads");

    // Path to yt-dlp executable
    private final String ytDlpPath;

    // Proxy environment variables
    private final String proxyHost = System.getenv("YT_PROXY_HOST");
    private final String proxyPort = System.getenv("YT_PROXY_PORT");
    private final String proxyUser = System.getenv("YT_PROXY_USER");
    private final String proxyPass = System.getenv("YT_PROXY_PASS");

    public YtService() throws IOException {
        Files.createDirectories(downloadDir);

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.ytDlpPath = "C:\\tools\\yt-dlp.exe"; // local Windows path
        } else {
            this.ytDlpPath = "yt-dlp"; // assume installed in Linux PATH
        }

        // Test yt-dlp
        try {
            ProcessBuilder pb = new ProcessBuilder(ytDlpPath, "--version");
            Process p = pb.start();
            int exit = p.waitFor();
            if (exit != 0) {
                System.err.println("yt-dlp exists but failed to run");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("yt-dlp executable not found! Please install it and ensure it's in PATH.");
        }
    }

    public String createJob(String url, String format, String quality) {
        String jobId = UUID.randomUUID().toString();
        JobStatus job = new JobStatus(jobId, "pending", null, null);
        jobs.put(jobId, job);

        executor.submit(() -> runDownloadJob(jobId, url, format, quality));

        return jobId;
    }

    private void runDownloadJob(String jobId, String url, String format, String quality) {
        JobStatus job = jobs.get(jobId);
        job.setState("running");

        try {
            String fileName = jobId + (format.equals("mp3") ? ".mp3" : ".mp4");
            Path outputFile = downloadDir.resolve(fileName);

            ProcessBuilder pb;

            // Build proxy argument if environment variables exist
            String proxyArg = null;
            if (proxyHost != null && proxyPort != null) {
                if (proxyUser != null && proxyPass != null) {
                    proxyArg = String.format("http://%s:%s@%s:%s", proxyUser, proxyPass, proxyHost, proxyPort);
                } else {
                    proxyArg = String.format("http://%s:%s", proxyHost, proxyPort);
                }
            }

            if (format.equals("mp3")) {
                pb = new ProcessBuilder(
                        ytDlpPath,
                        "-f", quality,
                        "--extract-audio",
                        "--audio-format", "mp3",
                        "-o", outputFile.toString(),
                        url
                );
            } else {
                pb = new ProcessBuilder(
                        ytDlpPath,
                        "-f", quality,
                        "-o", outputFile.toString(),
                        url
                );
            }

            if (proxyArg != null) {
                pb.command().add(1, "--proxy"); // insert --proxy argument after yt-dlp
                pb.command().add(2, proxyArg);  // insert proxy URL
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[yt-dlp] " + line);
            }

            int exit = process.waitFor();
            if (exit == 0) {
                job.setState("completed");
                job.setDownloadUrl("/downloads/" + fileName);
            } else {
                job.setState("failed");
                job.setError("Download failed: Exit code " + exit);
            }

        } catch (IOException e) {
            job.setState("failed");
            job.setError("yt-dlp not found or failed to run. Please ensure yt-dlp is installed and in PATH.");
        } catch (Exception e) {
            job.setState("failed");
            job.setError(e.getMessage());
        }
    }

    public JobStatus getJobStatus(String jobId) {
        return jobs.getOrDefault(jobId, new JobStatus(jobId, "not_found", null, null));
    }

    public boolean deleteJob(String jobId) {
        JobStatus job = jobs.remove(jobId);
        if (job == null || job.getDownloadUrl() == null) return false;

        try {
            Files.deleteIfExists(Paths.get(job.getDownloadUrl()));
        } catch (IOException ignored) {}

        return true;
    }
}
