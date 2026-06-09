package com.event.ledger.converter;

import com.event.ledger.model.ConsumerEvent;
import com.event.ledger.model.ConsumerEventEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEventConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ConsumerEventEntity toEntity(ConsumerEvent consumerEvent) {
        if (consumerEvent == null) {
            return null;
        }

        ConsumerEventEntity entity = new ConsumerEventEntity();
        entity.setEventId(consumerEvent.getEventId());
        entity.setAccountId(consumerEvent.getAccountId());
        entity.setType(consumerEvent.getType());
        entity.setAmount(consumerEvent.getAmount());
        entity.setCurrency(consumerEvent.getCurrency());
        entity.setEventTimestamp(consumerEvent.getEventTimestamp());

        if (consumerEvent.getMetadata() != null) {
            try {
                String metadataJson = objectMapper.writeValueAsString(consumerEvent.getMetadata());
                entity.setMetadata(metadataJson);
            } catch (Exception e) {
                entity.setMetadata(null);
            }
        }

        return entity;
    }

    public ConsumerEvent toDto(ConsumerEventEntity entity) {
        if (entity == null) {
            return null;
        }

        ConsumerEvent dto = new ConsumerEvent();
        dto.setEventId(entity.getEventId());
        dto.setAccountId(entity.getAccountId());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        dto.setEventTimestamp(entity.getEventTimestamp());

        if (entity.getMetadata() != null) {
            try {
                Object metadata = objectMapper.readValue(entity.getMetadata(), Object.class);
                dto.setMetadata((java.util.Map<String, Object>) metadata);
            } catch (Exception e) {
                dto.setMetadata(null);
            }
        }

        return dto;
    }
}
