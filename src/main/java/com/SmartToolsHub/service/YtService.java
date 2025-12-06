package com.SmartToolsHub.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Service
public class YtService {

    private final Map<String, JobStatus> jobs = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    // Render-safe directory
    private final Path downloadDir = Paths.get("/tmp/downloads");

    private final String ytDlpPath;

    public YtService() throws IOException {
        Files.createDirectories(downloadDir);

        // Detect yt-dlp
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.ytDlpPath = "C:\\tools\\yt-dlp.exe";
        } else {
            this.ytDlpPath = "yt-dlp";
        }

        testYtDlp();
    }

    private void testYtDlp() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ytDlpPath, "--version");
            Process p = pb.start();
            p.waitFor();
            System.out.println("yt-dlp ready");
        } catch (Exception e) {
            System.err.println("yt-dlp not found on Render!");
        }
    }

    // Build proxy URL from environment variables
    private String getProxyFromEnv() {
        String host = System.getenv("YT_PROXY_HOST");
        String port = System.getenv("YT_PROXY_PORT");
        String user = System.getenv("YT_PROXY_USER");
        String pass = System.getenv("YT_PROXY_PASS");

        if (host == null || port == null)
            return null;

        if (user != null && pass != null) {
            return "http://" + user + ":" + pass + "@" + host + ":" + port;
        }

        return "http://" + host + ":" + port;
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

            // **Single Webshare Proxy**
            String proxy = getProxyFromEnv();
            System.out.println("Using proxy: " + proxy);

            List<String> command = new ArrayList<>();
            command.add(ytDlpPath);

            // add proxy
            if (proxy != null) {
                command.add("--proxy");
                command.add(proxy);
            }

            // quality
            command.add("-f");
            command.add(quality);

            // mp3 conversion
            if (format.equals("mp3")) {
                command.add("--extract-audio");
                command.add("--audio-format");
                command.add("mp3");
            }

            // output
            command.add("-o");
            command.add(outputFile.toString());
            command.add(url);

            ProcessBuilder pb = new ProcessBuilder(command);
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
                job.setDownloadUrl("/api/yt/downloads/" + fileName);
            } else {
                job.setState("failed");
                job.setError("Download failed: exit " + exit);
            }

        } catch (Exception e) {
            job.setState("failed");
            job.setError(e.getMessage());
        }
    }

    public JobStatus getJobStatus(String jobId) {
        return jobs.getOrDefault(jobId, new JobStatus(jobId, "not_found", null, null));
    }
}
