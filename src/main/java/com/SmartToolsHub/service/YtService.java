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

    // Proxy rotation list
    private final List<String> proxyList = new ArrayList<>();
    private int proxyIndex = 0;

    public YtService() throws IOException {
        Files.createDirectories(downloadDir);

        // Detect yt-dlp
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.ytDlpPath = "C:\\tools\\yt-dlp.exe";
        } else {
            this.ytDlpPath = "yt-dlp";
        }

        loadProxyList();
        testYtDlp();
    }

    private void testYtDlp() {
        try {
            ProcessBuilder pb = new ProcessBuilder(ytDlpPath, "--version");
            Process p = pb.start();
            p.waitFor();
            System.out.println("yt-dlp detected successfully.");
        } catch (Exception e) {
            System.err.println("yt-dlp executable not found or not working on Render!");
        }
    }

    private void loadProxyList() {
        String list = System.getenv("YT_PROXY_LIST");
        if (list != null && !list.isEmpty()) {
            String[] arr = list.split(",");
            for (String item : arr) {
                item = item.trim();
                if (!item.isEmpty()) {
                    proxyList.add(item);
                }
            }
        }
        System.out.println("Loaded proxies: " + proxyList.size());
    }

    // Get next proxy (round-robin)
    private synchronized String getNextProxy() {
        if (proxyList.isEmpty()) return null;

        String raw = proxyList.get(proxyIndex);
        proxyIndex = (proxyIndex + 1) % proxyList.size();

        String[] parts = raw.split(":");
        if (parts.length == 4) {
            String ip = parts[0];
            String port = parts[1];
            String user = parts[2];
            String pass = parts[3];
            return "http://" + user + ":" + pass + "@" + ip + ":" + port;
        }

        return null;
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

            // Rotating Proxy
            String proxyArg = getNextProxy();
            System.out.println("Using proxy: " + proxyArg);

            List<String> command = new ArrayList<>();
            command.add(ytDlpPath);

            if (proxyArg != null) {
                command.add("--proxy");
                command.add(proxyArg);
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
