package com.SmartToolsHub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SmartToolsHub.dto.DownloadRequest;
import com.SmartToolsHub.service.JobStatus;
import com.SmartToolsHub.service.YtService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


@RestController
@RequestMapping("/api/yt")
public class YtController {


@Autowired
private YtService ytService;


@PostMapping("/download")
public ResponseEntity<?> startDownload(@RequestBody DownloadRequest req) {
// add auth check / rate limit here
String jobId = ytService.createJob(req.getUrl(), req.getFormat(), req.getQuality());
return ResponseEntity.ok(Map.of("jobId", jobId));
}


@GetMapping("/status/{jobId}")
public ResponseEntity<?> status(@PathVariable String jobId) {
JobStatus s = ytService.getJobStatus(jobId);
return ResponseEntity.ok(s);
}
@GetMapping("/downloads/{filename:.+}")
public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
    Path file = Paths.get("downloads").resolve(filename);
    if (!Files.exists(file)) {
        return ResponseEntity.notFound().build();
    }
    Resource resource = new UrlResource(file.toUri());
    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
            .body(resource);
}
}


// DTOs
