package com.event.ledger.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.event.ledger.validator.ValidEventId;
import java.util.Map;

public class ConsumerEvent {

    @NotBlank(message = "Event id cannot be empty")
    @ValidEventId
    @JsonProperty("eventId")
    private String eventId;

    @NotBlank(message = "accountId is required")
    @JsonProperty("accountId")
    private String accountId;

    @NotNull(message = "type is required")
    @Pattern(regexp = "CREDIT|DEBIT", message = "type must be either CREDIT or DEBIT")
    @JsonProperty("type")
    private String type;

    @NotNull(message = "amount is required")
    @Positive(message = "amount cannot be zero or negative")
    @JsonProperty("amount")
    private Double amount;

    @NotBlank(message = "currency is required")
    @JsonProperty("currency")
    private String currency;

    @NotBlank(message = "eventTimestamp is required")
    @JsonProperty("eventTimestamp")
    private String eventTimestamp;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    public ConsumerEvent() {
    }

    public ConsumerEvent(String eventId, String accountId, String type, Double amount,
                        String currency, String eventTimestamp) {
        this.eventId = eventId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.eventTimestamp = eventTimestamp;
    }

    public ConsumerEvent(String eventId, String accountId, String type, Double amount,
                        String currency, String eventTimestamp, Map<String, Object> metadata) {
        this.eventId = eventId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.eventTimestamp = eventTimestamp;
        this.metadata = metadata;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(String eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ConsumerEvent{" +
                "eventId='" + eventId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", eventTimestamp='" + eventTimestamp + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
