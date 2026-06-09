package com.echocano.tennis.league.infrastructure.adapters.in.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private List<String> details;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(String message, int status, List<String> details) {
        this(message, status);
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getDetails() {
        return details;
    }

}
