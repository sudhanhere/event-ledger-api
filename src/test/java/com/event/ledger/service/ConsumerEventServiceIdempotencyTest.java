package com.event.ledger.service;

import com.event.ledger.model.ConsumerEventEntity;
import com.event.ledger.repository.ConsumerEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ConsumerEventServiceIdempotencyTest {

    @Autowired
    private ConsumerEventService consumerEventService;

    @Autowired
    private ConsumerEventRepository consumerEventRepository;

    private ConsumerEventEntity testEvent;

    @BeforeEach
    void setUp() {
        consumerEventRepository.deleteAll();
        testEvent = new ConsumerEventEntity(
                "event-123",
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:30:00"
        );
    }

    @Test
    void testIdempotencyFirstSubmission() {
        ConsumerEventEntity savedEvent = consumerEventService.createConsumerEventIdempotent(testEvent);
        assertNotNull(savedEvent);
        assertEquals("event-123", savedEvent.getEventId());
        assertEquals("account-001", savedEvent.getAccountId());
    }

    @Test
    void testIdempotencyDuplicateSubmission() {
        // First submission
        ConsumerEventEntity savedEvent1 = consumerEventService.createConsumerEventIdempotent(testEvent);
        assertNotNull(savedEvent1);

        // Duplicate submission with same event ID
        ConsumerEventEntity savedEvent2 = consumerEventService.createConsumerEventIdempotent(testEvent);

        // Should return the same event, not create a new one
        assertEquals(savedEvent1.getEventId(), savedEvent2.getEventId());
        assertEquals(savedEvent1.getCreatedAt(), savedEvent2.getCreatedAt());
    }

    @Test
    void testIdempotencyMultipleDuplicateSubmissions() {
        // First submission
        ConsumerEventEntity savedEvent1 = consumerEventService.createConsumerEventIdempotent(testEvent);

        // Multiple duplicate submissions
        for (int i = 0; i < 5; i++) {
            ConsumerEventEntity savedEvent = consumerEventService.createConsumerEventIdempotent(testEvent);
            assertEquals(savedEvent1.getEventId(), savedEvent.getEventId());
        }

        // Verify only one event exists in database
        assertEquals(1, consumerEventRepository.findAll().size());
    }

    @Test
    void testIdempotencyDifferentEventsForSameAccount() {
        // Create first event
        ConsumerEventEntity event1 = new ConsumerEventEntity(
                "event-123",
                "account-001",
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T10:30:00"
        );
        ConsumerEventEntity saved1 = consumerEventService.createConsumerEventIdempotent(event1);

        // Create different event for same account
        ConsumerEventEntity event2 = new ConsumerEventEntity(
                "event-124",
                "account-001",
                "DEBIT",
                50.0,
                "USD",
                "2024-06-10T11:30:00"
        );
        ConsumerEventEntity saved2 = consumerEventService.createConsumerEventIdempotent(event2);

        // Both should be created
        assertNotEquals(saved1.getEventId(), saved2.getEventId());
        assertEquals(2, consumerEventRepository.findAll().size());
    }
}
