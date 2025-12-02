package com.SmartToolsHub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.SmartToolsHub.model.AiRequest;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final WebClient webClient;
    private final String openAiKey;
    private final String model;

    public AiService(@Value("${openai.api.key}") String openAiKey,
                     @Value("${openai.model:gpt-4o-mini}") String model) {
        this.openAiKey = openAiKey;
        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + openAiKey)
                .build();
    }

    // MAIN ROUTER
    public String handle(AiRequest req) {
        String action = req.getAction();
        String input = req.getInput();

        if (action == null) return "No action provided.";

        return switch (action) {

            // OLD ACTIONS
            case "summarize"    -> summarize(input, req.getMode());
            case "rewrite"      -> rewrite(input, req.getTone());
            case "paraphrase"   -> paraphrase(input, req.getStyle());
            case "explain"      -> explainCode(input, req.getLang());

            // NEW ADDED TOOLS
            case "grammarFix"   -> grammarFix(input);
            case "codeGenerator"-> codeGenerator(input, req.getLang());
            case "textToSQL"    -> textToSQL(input);
            case "translate"    -> translate(input, req.getLang());
            case "keywords"     -> extractKeywords(input);
            case "emailWriter"  -> emailWriter(input, req.getTone());
            case "blogWriter"   -> blogWriter(input);
            case "debugCode"    -> debugCode(input);
            case "formatData"   -> dataFormatter(input);
            case "formatJson"   -> jsonFormatter(input);
            case "improveResume"-> resumePointsImprover(input);
            case "math"         -> mathSolver(input);

            default -> "❌ Unknown AI Tool: " + action;
        };
    }


    // CHATBOT
    public String chat(String message) {
        String prompt = "You are an intelligent assistant.\nUser: " + message;
        return callChatCompletion(prompt);
    }

    // -------------------- AI TOOL IMPLEMENTATIONS -------------------- //

    private String summarize(String text, String mode) {
        String type = (mode == null ? "short" : mode);
        String prompt = switch (type) {
            case "bullet" -> "Summarize in bullet points:\n" + text;
            case "detailed" -> "Give a detailed summary:\n" + text;
            default -> "Summarize in 2-3 sentences:\n" + text;
        };
        return callChatCompletion(prompt);
    }

    private String rewrite(String text, String tone) {
        tone = (tone == null ? "professional" : tone);
        return callChatCompletion(
                "Rewrite this in a " + tone + " tone:\n" + text
        );
    }

    private String paraphrase(String text, String style) {
        style = (style == null ? "formal" : style);
        return callChatCompletion(
                "Paraphrase this in a " + style + " style:\n" + text
        );
    }

    private String explainCode(String code, String lang) {
        return callChatCompletion(
                "Explain the following " + (lang == null ? "" : lang) + " code in detail:\n" + code
        );
    }

    private String grammarFix(String text) {
        return callChatCompletion("Fix grammar and return improved text:\n" + text);
    }

    private String codeGenerator(String description, String lang) {
        lang = (lang == null ? "Java" : lang);
        return callChatCompletion(
                "Generate " + lang + " code based on this description:\n" + description
        );
    }

    private String textToSQL(String input) {
        return callChatCompletion(
                "Convert this request into SQL query only:\n" + input
        );
    }

    private String translate(String text, String lang) {
        if (lang == null) lang = "Tamil";
        return callChatCompletion("Translate to " + lang + ":\n" + text);
    }

    private String extractKeywords(String text) {
        return callChatCompletion("Extract top keywords from this text:\n" + text);
    }

    private String emailWriter(String points, String tone) {
        tone = tone == null ? "professional" : tone;
        return callChatCompletion(
                "Write a " + tone + " email based on these points:\n" + points
        );
    }

    private String blogWriter(String topic) {
        return callChatCompletion("Write a blog article about:\n" + topic);
    }

    private String debugCode(String code) {
        return callChatCompletion("Find bugs and fix this code:\n" + code);
    }

    private String dataFormatter(String text) {
        return callChatCompletion("Format this data cleanly:\n" + text);
    }

    private String jsonFormatter(String text) {
        return callChatCompletion("Format this content into pretty JSON:\n" + text);
    }

    private String resumePointsImprover(String points) {
        return callChatCompletion("Improve these resume bullet points:\n" + points);
    }

    private String mathSolver(String query) {
        return callChatCompletion("Solve this math problem step-by-step:\n" + query);
    }


    // -------------------- OPENAI API CALL -------------------- //
    private String callChatCompletion(String prompt) {

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.2,
                "max_tokens", 800
        );

        try {
            Map resp = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(Duration.ofSeconds(30));

            if (resp == null) return "⚠ No response from OpenAI.";

            List choices = (List) resp.get("choices");
            if (choices == null || choices.isEmpty()) return "⚠ No choices returned.";

            Map first = (Map) choices.get(0);
            Map msg = (Map) first.get("message");

            return msg != null ? (String) msg.get("content") : (String) first.get("text");

        } catch (Exception ex) {
            ex.printStackTrace();
            return "❌ AI Error: " + ex.getMessage();
        }
    }

}
