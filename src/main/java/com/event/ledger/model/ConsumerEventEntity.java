package com.event.ledger.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "consumer_events", indexes = {
        @Index(name = "idx_account_id", columnList = "account_id"),
        @Index(name = "idx_event_timestamp", columnList = "event_timestamp"),
        @Index(name = "idx_type", columnList = "type")
})
public class ConsumerEventEntity {

    @Id
    @Column(name = "event_id", length = 255)
    @NotBlank(message = "eventId is required")
    @JsonProperty("eventId")
    private String eventId;

    @Column(name = "account_id", nullable = false, length = 255)
    @NotBlank(message = "accountId is required")
    @JsonProperty("accountId")
    private String accountId;

    @Column(name = "type", nullable = false, length = 10)
    @NotNull(message = "type is required")
    @Pattern(regexp = "CREDIT|DEBIT", message = "type must be either CREDIT or DEBIT")
    @JsonProperty("type")
    private String type;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than 0")
    @JsonProperty("amount")
    private Double amount;

    @Column(name = "currency", nullable = false, length = 10)
    @NotBlank(message = "currency is required")
    @JsonProperty("currency")
    private String currency;

    @Column(name = "event_timestamp", nullable = false, length = 50)
    @NotBlank(message = "eventTimestamp is required")
    @JsonProperty("eventTimestamp")
    private String eventTimestamp;

    @Column(name = "metadata", columnDefinition = "CLOB")
    @JsonProperty("metadata")
    private String metadata;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public ConsumerEventEntity() {
    }

    public ConsumerEventEntity(String eventId, String accountId, String type, Double amount,
                        String currency, String eventTimestamp) {
        this.eventId = eventId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.eventTimestamp = eventTimestamp;
    }

    public ConsumerEventEntity(String eventId, String accountId, String type, Double amount,
                        String currency, String eventTimestamp, String metadata) {
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

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ConsumerEventEntity{" +
                "eventId='" + eventId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", eventTimestamp='" + eventTimestamp + '\'' +
                ", metadata='" + metadata + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
