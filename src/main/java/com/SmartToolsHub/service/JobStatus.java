package com.SmartToolsHub.service;

public class JobStatus {
    private String jobId;
    private String state;       // PROCESSING, DONE, FAILED
    private String downloadUrl; // URL used by frontend
    private String error;

    public JobStatus(String jobId, String state, String downloadUrl, String error) {
        this.jobId = jobId;
        this.state = state;
        this.downloadUrl = downloadUrl;
        this.error = error;
    }

    public String getJobId() { return jobId; }
    public String getState() { return state; }
    public String getDownloadUrl() { return downloadUrl; }
    public String getError() { return error; }

    public void setState(String state) { this.state = state; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    public void setError(String error) { this.error = error; }
}
