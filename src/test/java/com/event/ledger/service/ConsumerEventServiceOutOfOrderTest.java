package com.event.ledger.service;

import com.event.ledger.model.ConsumerEventEntity;
import com.event.ledger.repository.ConsumerEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ConsumerEventServiceOutOfOrderTest {

    @Autowired
    private ConsumerEventService consumerEventService;

    @Autowired
    private ConsumerEventRepository consumerEventRepository;

    private String accountId = "account-001";

    @BeforeEach
    void setUp() {
        consumerEventRepository.deleteAll();
    }

    @Test
    void testOutOfOrderEventArrivalChronologicalOrder() {
        // Create events with timestamps in non-chronological order
        ConsumerEventEntity event3 = new ConsumerEventEntity(
                "event-003",
                accountId,
                "CREDIT",
                300.0,
                "USD",
                "2024-06-10T14:00:00"
        );

        ConsumerEventEntity event1 = new ConsumerEventEntity(
                "event-001",
                accountId,
                "CREDIT",
                100.0,
                "USD",
                "2024-06-10T12:00:00"
        );

        ConsumerEventEntity event2 = new ConsumerEventEntity(
                "event-002",
                accountId,
                "DEBIT",
                200.0,
                "USD",
                "2024-06-10T13:00:00"
        );

        // Submit events out of order: 3, 1, 2
        consumerEventService.createConsumerEvent(event3);
        consumerEventService.createConsumerEvent(event1);
        consumerEventService.createConsumerEvent(event2);

        // Retrieve events - should be in chronological order
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByAccountId(accountId);

        assertEquals(3, events.size());
        assertEquals("event-001", events.get(0).getEventId());
        assertEquals("2024-06-10T12:00:00", events.get(0).getEventTimestamp());
        
        assertEquals("event-002", events.get(1).getEventId());
        assertEquals("2024-06-10T13:00:00", events.get(1).getEventTimestamp());
        
        assertEquals("event-003", events.get(2).getEventId());
        assertEquals("2024-06-10T14:00:00", events.get(2).getEventTimestamp());
    }

    @Test
    void testOutOfOrderEventArrivalByType() {
        // Create events with timestamps in non-chronological order
        ConsumerEventEntity creditEvent2 = new ConsumerEventEntity(
                "event-c2",
                accountId,
                "CREDIT",
                500.0,
                "USD",
                "2024-06-10T14:00:00"
        );

        ConsumerEventEntity debitEvent = new ConsumerEventEntity(
                "event-d1",
                accountId,
                "DEBIT",
                100.0,
                "USD",
                "2024-06-10T13:00:00"
        );

        ConsumerEventEntity creditEvent1 = new ConsumerEventEntity(
                "event-c1",
                accountId,
                "CREDIT",
                300.0,
                "USD",
                "2024-06-10T12:00:00"
        );

        // Submit out of order
        consumerEventService.createConsumerEvent(creditEvent2);
        consumerEventService.createConsumerEvent(debitEvent);
        consumerEventService.createConsumerEvent(creditEvent1);

        // Get CREDIT events only - should be in chronological order
        List<ConsumerEventEntity> creditEvents = consumerEventService.getConsumerEventsByAccountIdAndType(accountId, "CREDIT");

        assertEquals(2, creditEvents.size());
        assertEquals("event-c1", creditEvents.get(0).getEventId());
        assertEquals("2024-06-10T12:00:00", creditEvents.get(0).getEventTimestamp());
        
        assertEquals("event-c2", creditEvents.get(1).getEventId());
        assertEquals("2024-06-10T14:00:00", creditEvents.get(1).getEventTimestamp());
    }

    @Test
    void testOutOfOrderEventsWithSameTimestamp() {
        // Create events with same timestamp but different event IDs
        String sameTimestamp = "2024-06-10T12:00:00";
        
        ConsumerEventEntity event1 = new ConsumerEventEntity("event-1", accountId, "CREDIT", 100.0, "USD", sameTimestamp);
        ConsumerEventEntity event2 = new ConsumerEventEntity("event-2", accountId, "CREDIT", 200.0, "USD", sameTimestamp);
        ConsumerEventEntity event3 = new ConsumerEventEntity("event-3", accountId, "CREDIT", 300.0, "USD", sameTimestamp);

        // Submit out of order
        consumerEventService.createConsumerEvent(event3);
        consumerEventService.createConsumerEvent(event1);
        consumerEventService.createConsumerEvent(event2);

        // All should be retrieved (order among same timestamp is acceptable)
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByAccountId(accountId);
        assertEquals(3, events.size());
    }

    @Test
    void testReverseOrderEventArrival() {
        // Create 5 events with reversed timestamps
        for (int i = 5; i >= 1; i--) {
            ConsumerEventEntity event = new ConsumerEventEntity(
                    "event-" + i,
                    accountId,
                    i % 2 == 0 ? "CREDIT" : "DEBIT",
                    (double) (i * 100),
                    "USD",
                    "2024-06-10T" + (10 + i) + ":00:00"
            );
            consumerEventService.createConsumerEvent(event);
        }

        // Retrieve and verify chronological order
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByAccountId(accountId);
        
        assertEquals(5, events.size());
        for (int i = 0; i < 4; i++) {
            assertTrue(events.get(i).getEventTimestamp().compareTo(events.get(i + 1).getEventTimestamp()) <= 0);
        }
    }
}
