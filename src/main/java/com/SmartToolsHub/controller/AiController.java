package com.SmartToolsHub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SmartToolsHub.model.AiRequest;
import com.SmartToolsHub.model.SavedAiResult;
import com.SmartToolsHub.repository.SavedAiResultRepository;
import com.SmartToolsHub.service.AiService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final SavedAiResultRepository savedRepo;

    // UNIVERSAL ENDPOINT → handles: summarize, translate, code-generate, sql-query etc.
    @PostMapping
    public ResponseEntity<?> handleGeneric(@RequestBody AiRequest request) {

        String output = aiService.handle(request);

        // If the output looks like code → wrap in ``` for easy copy
        if (looksLikeCode(output)) {
            output = "```java\n" + output + "\n```";
        }

        return ResponseEntity.ok(Map.of("output", output));
    }

    // CHATBOT ENDPOINT
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody Map<String,String> body) {

        String message = body.get("message");
        String output = aiService.chat(message);

        // Auto wrap if it's code
        if (looksLikeCode(output)) {
            output = "```java\n" + output + "\n```";
        }

        return ResponseEntity.ok(Map.of("output", output));
    }

    // SAVE CHAT / AI RESULT
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SavedAiResult save) {
        savedRepo.save(save);
        return ResponseEntity.ok(Map.of("status", "saved"));
    }


    // ---------- HELPER (Detect if response is code) ----------
    private boolean looksLikeCode(String text) {
        if (text == null) return false;

        return text.contains(";")
                || text.contains("{")
                || text.contains("}")
                || text.contains("public class")
                || text.contains("function")
                || text.contains("SELECT ")
                || text.contains("INSERT ")
                || text.contains("UPDATE ")
                || text.contains("package ");
    }
}
