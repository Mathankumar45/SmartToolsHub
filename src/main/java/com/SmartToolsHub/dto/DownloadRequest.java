package com.SmartToolsHub.dto;

import lombok.Data;

@Data
public class DownloadRequest 
{ 
	private String url; 
	private String format; 
	private String quality; /* getters/setters */ 
}
