package com.SmartToolsHub.model;

import lombok.Data;

@Data
public class AiRequest {
    private String action; // summarize, rewrite, paraphrase, explain
    private String input;
    private String mode;
    private String tone;
    private String lang;
    private String style;
}
