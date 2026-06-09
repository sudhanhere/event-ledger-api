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

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public EventCreationResponse() {
    }

    public EventCreationResponse(boolean success, String message, String eventId) {
        this.success = success;
        this.message = message;
        this.eventId = eventId;
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
                ", timestamp=" + timestamp +
                '}';
    }
}
