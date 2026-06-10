package com.event.ledger.controller;

import com.event.ledger.model.ConsumerEvent;
import com.event.ledger.repository.ConsumerEventRepository;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ConsumerEventControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsumerEventRepository consumerEventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        consumerEventRepository.deleteAll();
    }

    @Test
    void testCreateEventWithMissingEventId() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                null, // missing eventId
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed. Please check the required fields."))
                .andExpect(jsonPath("$.errors[*].field", hasItem("eventId")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("Event id cannot be empty")));
    }

    @Test
    void testCreateEventWithEmptyEventId() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "", // empty eventId
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("eventId")));
    }

    @Test
    void testCreateEventWithInvalidEventIdTooShort() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "ab", // too short (less than 3 characters)
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].message", hasItem(containsString("at least 3 characters"))));
    }

    @Test
    void testCreateEventWithInvalidEventIdSpecialCharacters() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event@123!", // invalid characters
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].message", hasItem(containsString("invalid characters"))));
    }

    @Test
    void testCreateEventWithMissingAccountId() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                null, // missing accountId
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("accountId")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("accountId is required")));
    }

    @Test
    void testCreateEventWithMissingType() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                null, // missing type
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("type")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("type is required")));
    }

    @Test
    void testCreateEventWithInvalidType() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "INVALID", // invalid type
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].message", hasItem("type must be either CREDIT or DEBIT")));
    }

    @Test
    void testCreateEventWithMissingAmount() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                null, // missing amount
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("amount")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("amount is required")));
    }

    @Test
    void testCreateEventWithZeroAmount() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                0.0, // zero amount
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].message", hasItem("amount cannot be zero or negative")));
    }

    @Test
    void testCreateEventWithNegativeAmount() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                -100.0, // negative amount
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].message", hasItem("amount cannot be zero or negative")));
    }

    @Test
    void testCreateEventWithMissingCurrency() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                100.0,
                null, // missing currency
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("currency")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("currency is required")));
    }

    @Test
    void testCreateEventWithMissingEventTimestamp() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                null // missing eventTimestamp
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[*].field", hasItem("eventTimestamp")))
                .andExpect(jsonPath("$.errors[*].message", hasItem("eventTimestamp is required")));
    }

    @Test
    void testCreateEventWithValidData() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Consumer Event created successfully"))
                .andExpect(jsonPath("$.eventId").value("event-001"))
                .andExpect(jsonPath("$.isDuplicate").value(false));
    }

    @Test
    void testCreateEventWithValidEventIdFormats() throws Exception {
        String[] validEventIds = {"abc123", "event-001", "event_001", "EVENT_123"};

        for (String eventId : validEventIds) {
            consumerEventRepository.deleteAll();
            ConsumerEvent event = new ConsumerEvent(
                    eventId,
                    "account-001",
                    "CREDIT",
                    100.0,
                    "USD",
                    "2024-06-10T10:00:00"
            );

            mockMvc.perform(post("/api/v1/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true));
        }
    }

    @Test
    void testCreateDuplicateEvent() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "event-001",
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:00:00"
        );

        String eventJson = objectMapper.writeValueAsString(event);

        // First submission
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isDuplicate").value(false));

        // Duplicate submission
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.isDuplicate").value(true));
    }

    @Test
    void testCreateEventWithMultipleValidationErrors() throws Exception {
        ConsumerEvent event = new ConsumerEvent(
                "ab", // too short
                null, // missing accountId
                "INVALID", // invalid type
                0.0, // zero amount
                null, // missing currency
                null // missing eventTimestamp
        );

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.length()").value(greaterThanOrEqualTo(3)));
    }
}
