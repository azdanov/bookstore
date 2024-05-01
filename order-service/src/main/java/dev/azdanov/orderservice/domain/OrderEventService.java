package dev.azdanov.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    public OrderEventService(
            OrderEventRepository orderEventRepository,
            OrderEventPublisher orderEventPublisher,
            ObjectMapper objectMapper) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    public <T extends OrderEvent> void save(T event) {
        OrderEventEntity orderEvent = new OrderEventEntity();

        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(event.getEventType());
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));

        orderEventRepository.save(orderEvent);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);

        log.info("Found {} order events to be published", events.size());

        for (OrderEventEntity event : events) {
            try {
                publishEvent(event);
                orderEventRepository.delete(event);
            } catch (Exception e) {
                log.error("Failure during event publishing for event {}", event.getEventId(), e);
            }
        }
    }

    private void publishEvent(OrderEventEntity event) {
        Class<? extends OrderEvent> eventTypeClass = event.getEventType().getEventClass();
        OrderEvent orderEvent = fromJsonPayload(event.getPayload(), eventTypeClass);
        orderEventPublisher.publish(orderEvent);
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new OrderEventProcessingException("Failed to serialize event to JSON", e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new OrderEventProcessingException("Failed to deserialize JSON to event", e);
        }
    }
}
