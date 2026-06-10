package com.event.ledger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<FieldError> errors;

    @JsonProperty("timestamp")
    private Long timestamp;

    public ErrorResponse() {
        this.success = false;
        this.timestamp = System.currentTimeMillis();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String message) {
        this();
        this.message = message;
    }

    public ErrorResponse(String message, List<FieldError> errors) {
        this();
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void addError(FieldError error) {
        this.errors.add(error);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                ", timestamp=" + timestamp +
                '}';
    }

    public static class FieldError {
        @JsonProperty("field")
        private String field;

        @JsonProperty("message")
        private String message;

        public FieldError() {
        }

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "FieldError{" +
                    "field='" + field + '\'' +
                    ", message='" + message + '\'' +
                    '}';
        }
    }
}
