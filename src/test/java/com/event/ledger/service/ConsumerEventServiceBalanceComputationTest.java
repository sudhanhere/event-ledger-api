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
class ConsumerEventServiceBalanceComputationTest {

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
    void testBalanceComputationOnlyCredits() {
        // Create only CREDIT events
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "CREDIT", 100.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "CREDIT", 200.0, "USD", "2024-06-10T11:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-3", accountId, "CREDIT", 150.0, "USD", "2024-06-10T12:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(450.0, creditSum);
        assertEquals(0.0, debitSum);
        assertEquals(450.0, netBalance);
    }

    @Test
    void testBalanceComputationOnlyDebits() {
        // Create only DEBIT events
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "DEBIT", 50.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "DEBIT", 100.0, "USD", "2024-06-10T11:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-3", accountId, "DEBIT", 75.0, "USD", "2024-06-10T12:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(0.0, creditSum);
        assertEquals(225.0, debitSum);
        assertEquals(-225.0, netBalance);
    }

    @Test
    void testBalanceComputationMixedCreditsAndDebits() {
        // Create mixed CREDIT and DEBIT events
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "CREDIT", 500.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "DEBIT", 100.0, "USD", "2024-06-10T11:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-3", accountId, "CREDIT", 300.0, "USD", "2024-06-10T12:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-4", accountId, "DEBIT", 150.0, "USD", "2024-06-10T13:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(800.0, creditSum);
        assertEquals(250.0, debitSum);
        assertEquals(550.0, netBalance);
    }

    @Test
    void testBalanceComputationZeroBalance() {
        // Create events that result in zero balance
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "CREDIT", 1000.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "DEBIT", 1000.0, "USD", "2024-06-10T11:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(1000.0, creditSum);
        assertEquals(1000.0, debitSum);
        assertEquals(0.0, netBalance);
    }

    @Test
    void testBalanceComputationEmptyAccount() {
        // No events for this account
        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(0.0, creditSum);
        assertEquals(0.0, debitSum);
        assertEquals(0.0, netBalance);
    }

    @Test
    void testBalanceComputationMultipleDecimalPlaces() {
        // Test with decimal amounts
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "CREDIT", 100.50, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "CREDIT", 250.75, "USD", "2024-06-10T11:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-3", accountId, "DEBIT", 50.25, "USD", "2024-06-10T12:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(351.25, creditSum);
        assertEquals(50.25, debitSum);
        assertEquals(301.0, netBalance);
    }

    @Test
    void testBalanceComputationForDifferentAccounts() {
        String account1 = "account-001";
        String account2 = "account-002";

        // Create events for account 1
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", account1, "CREDIT", 500.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", account1, "DEBIT", 200.0, "USD", "2024-06-10T11:00:00")
        );

        // Create events for account 2
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-3", account2, "CREDIT", 1000.0, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-4", account2, "DEBIT", 300.0, "USD", "2024-06-10T11:00:00")
        );

        // Verify balances are calculated separately for each account
        Double balance1 = consumerEventService.calculateNetBalance(account1);
        Double balance2 = consumerEventService.calculateNetBalance(account2);

        assertEquals(300.0, balance1);
        assertEquals(700.0, balance2);
    }

    @Test
    void testBalanceComputationLargeNumbers() {
        // Test with large amounts
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-1", accountId, "CREDIT", 999999.99, "USD", "2024-06-10T10:00:00")
        );
        consumerEventService.createConsumerEvent(
                new ConsumerEventEntity("event-2", accountId, "DEBIT", 123456.78, "USD", "2024-06-10T11:00:00")
        );

        Double creditSum = consumerEventService.calculateCreditSum(accountId);
        Double debitSum = consumerEventService.calculateDebitSum(accountId);
        Double netBalance = consumerEventService.calculateNetBalance(accountId);

        assertEquals(999999.99, creditSum);
        assertEquals(123456.78, debitSum);
        assertEquals(876543.21, netBalance, 0.01); // Allow small floating point tolerance
    }
}
