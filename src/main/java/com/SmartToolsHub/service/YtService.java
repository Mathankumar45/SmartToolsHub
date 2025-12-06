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

    private final Path downloadDir = Paths.get("/tmp/downloads");
    private final String ytDlpPath;

    private final Random random = new Random();

    public YtService() throws IOException {
        Files.createDirectories(downloadDir);

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

    // ✅ NEW — Read YT_PROXY_LIST from Render and pick one randomly
    private String getProxyFromEnv() {
        String list = System.getenv("YT_PROXY_LIST");

        if (list == null || list.isBlank()) {
            return null;
        }

        String[] proxies = list.split("\\r?\\n");

        String proxy = proxies[random.nextInt(proxies.length)].trim();

        // Convert to proper format if using "ip:port:user:pass"
        if (!proxy.startsWith("http://") && proxy.contains(":")) {
            String[] p = proxy.split(":");
            if (p.length == 4) {
                return "http://" + p[2] + ":" + p[3] + "@" + p[0] + ":" + p[1];
            }
        }

        return proxy;
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

            String proxy = getProxyFromEnv();
            System.out.println("Using proxy: " + proxy);

            List<String> command = new ArrayList<>();
            command.add(ytDlpPath);

            if (proxy != null) {
                command.add("--proxy");
                command.add(proxy);
            }

            command.add("-f");
            command.add(quality);

            if (format.equals("mp3")) {
                command.add("--extract-audio");
                command.add("--audio-format");
                command.add("mp3");
            }

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
