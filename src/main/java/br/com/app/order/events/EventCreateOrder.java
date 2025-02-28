package br.com.app.order.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventCreateOrder {

    private final Integer orderId;
    private final String name;
    private final LocalDateTime order_date;
    private final BigDecimal total_value;
    private final String status;
    private final LocalDateTime timestamp;

    public EventCreateOrder() {
        this.orderId = null;
        this.name = null;
        this.order_date = null;
        this.total_value = null;
        this.status = null;
        this.timestamp = null;
    }

    public EventCreateOrder(Integer orderId, String name, LocalDateTime order_date, BigDecimal total_value, String status, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.name = name;
        this.order_date = order_date;
        this.total_value = total_value;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public BigDecimal getTotal_value() {
        return total_value;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}