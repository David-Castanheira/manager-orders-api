package br.com.app.order.services;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.events.EventUpdateOrder;
import br.com.app.order.models.EventOrderEntity;
import br.com.app.order.repositories.EventOrderRepository;

@Service
public class EventOrderService {

    @Autowired
    private EventOrderRepository eventOrderRepository;

    private final ObjectMapper objectMapper;

    public EventOrderService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void createEventOrder(EventCreateOrder event) {
        saveEvent(event, "EventCreateOrder");
    }

    public void updateEventOrder(EventUpdateOrder event) {
        saveEvent(event, "EventUpdateOrder");
    }

    public void cancelEventOrder(EventCancelOrder event) {
        saveEvent(event, "EventCancelOrder");
    }

    private void saveEvent(Object event, String eventType) {
        try {
            String dataEvent = objectMapper.writeValueAsString(event);
            EventOrderEntity entityEvent = new EventOrderEntity();
            if (event instanceof EventCreateOrder) {
                entityEvent.setOrderId(((EventCreateOrder) event).getOrderId());
            } else if (event instanceof EventUpdateOrder) {
                entityEvent.setOrderId(((EventUpdateOrder) event).getOrderId());
            } else if (event instanceof EventCancelOrder) {
                entityEvent.setOrderId(((EventCancelOrder) event).getOrderId());
            }

            entityEvent.setEventType(eventType);
            entityEvent.setEventData(dataEvent);
            entityEvent.setTimestamp(LocalDateTime.now());
            eventOrderRepository.save(entityEvent);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível serializar o evento", e);
        }
    }

    public String serializeEvent(Object event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (IOException e) {
            throw new RuntimeException("Event not serialized", e);
        }
    }

    public<T> T deserializeEvent(String dataEvent, Class<T> classEvent) {
        try {
            return objectMapper.readValue(dataEvent, classEvent);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível desserializar o evento", e);
        }
    }

}
