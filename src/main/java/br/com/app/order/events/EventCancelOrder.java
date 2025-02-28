package br.com.app.order.events;

import java.time.LocalDateTime;

public class EventCancelOrder {

    private final Integer orderId;
    private final LocalDateTime timestamp;

    public EventCancelOrder() {
        this.orderId = null;
        this.timestamp = null;
    }

    public EventCancelOrder(Integer orderId, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.timestamp = timestamp;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
