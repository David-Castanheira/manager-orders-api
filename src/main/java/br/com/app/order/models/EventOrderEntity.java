package br.com.app.order.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_events_order")
public class EventOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long eventId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "event_type", length = 255, nullable = false)
    private String eventType;

    @Column(name = "event_data")
    private String eventData;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public EventOrderEntity() {
    }

    public EventOrderEntity(Integer orderId, String eventType, String eventData, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.eventData = eventData;
        this.timestamp = timestamp;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}