package com.event.ledger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class EventCreationResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("isDuplicate")
    private boolean isDuplicate;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public EventCreationResponse() {
    }

    public EventCreationResponse(boolean success, String message, String eventId) {
        this.success = success;
        this.message = message;
        this.eventId = eventId;
        this.isDuplicate = false;
        this.timestamp = LocalDateTime.now();
    }

    public EventCreationResponse(boolean success, String message, String eventId, boolean isDuplicate) {
        this.success = success;
        this.message = message;
        this.eventId = eventId;
        this.isDuplicate = isDuplicate;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EventCreationResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", eventId='" + eventId + '\'' +
                ", isDuplicate=" + isDuplicate +
                ", timestamp=" + timestamp +
                '}';
    }
}

