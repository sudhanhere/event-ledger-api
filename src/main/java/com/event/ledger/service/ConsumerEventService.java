package com.event.ledger.service;

import com.event.ledger.model.ConsumerEventEntity;
import com.event.ledger.repository.ConsumerEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerEventService {

    @Autowired
    private ConsumerEventRepository consumerEventRepository;

    public ConsumerEventEntity createConsumerEvent(ConsumerEventEntity consumerEvent) {
        return consumerEventRepository.save(consumerEvent);
    }

    public ConsumerEventEntity createConsumerEventIdempotent(ConsumerEventEntity consumerEvent) {
        Optional<ConsumerEventEntity> existingEvent = consumerEventRepository.findByEventId(consumerEvent.getEventId());
        if (existingEvent.isPresent()) {
            return existingEvent.get();
        }
        return consumerEventRepository.save(consumerEvent);
    }

    public Optional<ConsumerEventEntity> getConsumerEventById(String eventId) {
        return consumerEventRepository.findByEventId(eventId);
    }

    public List<ConsumerEventEntity> getConsumerEventsByAccountId(String accountId) {
        return consumerEventRepository.findByAccountIdOrderByTimestamp(accountId);
    }

    public List<ConsumerEventEntity> getConsumerEventsByAccountIdAndType(String accountId, String type) {
        return consumerEventRepository.findByAccountIdAndTypeOrderByTimestamp(accountId, type);
    }

    public List<ConsumerEventEntity> getConsumerEventsByType(String type) {
        return consumerEventRepository.findByType(type);
    }

    public List<ConsumerEventEntity> getAllConsumerEvents() {
        return consumerEventRepository.findAll();
    }

    public void deleteConsumerEvent(String eventId) {
        consumerEventRepository.deleteById(eventId);
    }

    public Double calculateNetBalance(String accountId) {
        return consumerEventRepository.calculateNetBalanceByAccountId(accountId);
    }

    public Double calculateCreditSum(String accountId) {
        List<ConsumerEventEntity> events = consumerEventRepository.findByAccountId(accountId);
        return events.stream()
                .filter(e -> "CREDIT".equals(e.getType()))
                .mapToDouble(ConsumerEventEntity::getAmount)
                .sum();
    }

    public Double calculateDebitSum(String accountId) {
        List<ConsumerEventEntity> events = consumerEventRepository.findByAccountId(accountId);
        return events.stream()
                .filter(e -> "DEBIT".equals(e.getType()))
                .mapToDouble(ConsumerEventEntity::getAmount)
                .sum();
    }
}

