package com.event.ledger.controller;

import com.event.ledger.converter.ConsumerEventConverter;
import com.event.ledger.dto.EventCreationResponse;
import com.event.ledger.model.ConsumerEvent;
import com.event.ledger.model.ConsumerEventEntity;
import com.event.ledger.service.ConsumerEventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class ConsumerEventController {

    @Autowired
    private ConsumerEventService consumerEventService;

    @Autowired
    private ConsumerEventConverter consumerEventConverter;

    @PostMapping
    public ResponseEntity<EventCreationResponse> createConsumerEvent(
            @Valid @RequestBody ConsumerEvent consumerEvent) {
        try {
            ConsumerEventEntity entity = consumerEventConverter.toEntity(consumerEvent);
            Optional<ConsumerEventEntity> existingEvent = consumerEventService.getConsumerEventById(entity.getEventId());
            
            if (existingEvent.isPresent()) {
                EventCreationResponse response = new EventCreationResponse(
                        true,
                        "Consumer Event already exists (duplicate submission)",
                        existingEvent.get().getEventId(),
                        true
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            
            ConsumerEventEntity savedEvent = consumerEventService.createConsumerEventIdempotent(entity);
            EventCreationResponse response = new EventCreationResponse(
                    true,
                    "Consumer Event created successfully",
                    savedEvent.getEventId(),
                    false
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            EventCreationResponse errorResponse = new EventCreationResponse(
                    false,
                    "Error creating consumer event: " + e.getMessage(),
                    null,
                    false
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ConsumerEvent> getConsumerEventById(@PathVariable String eventId) {
        Optional<ConsumerEventEntity> event = consumerEventService.getConsumerEventById(eventId);
        return event.map(e -> ResponseEntity.ok(consumerEventConverter.toDto(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ConsumerEvent>> getConsumerEventsByAccountId(
            @PathVariable String accountId) {
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByAccountId(accountId);
        List<ConsumerEvent> dtos = events.stream()
                .map(consumerEventConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/account/{accountId}/type/{type}")
    public ResponseEntity<List<ConsumerEvent>> getConsumerEventsByAccountIdAndType(
            @PathVariable String accountId,
            @PathVariable String type) {
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByAccountIdAndType(accountId, type);
        List<ConsumerEvent> dtos = events.stream()
                .map(consumerEventConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ConsumerEvent>> getConsumerEventsByType(@PathVariable String type) {
        List<ConsumerEventEntity> events = consumerEventService.getConsumerEventsByType(type);
        List<ConsumerEvent> dtos = events.stream()
                .map(consumerEventConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public ResponseEntity<List<ConsumerEvent>> getAllConsumerEvents() {
        List<ConsumerEventEntity> events = consumerEventService.getAllConsumerEvents();
        List<ConsumerEvent> dtos = events.stream()
                .map(consumerEventConverter::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteConsumerEvent(@PathVariable String eventId) {
        consumerEventService.deleteConsumerEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
