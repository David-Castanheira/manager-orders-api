package br.com.app.order.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventCreateOrder {

    private Integer orderId;
    private String name;
    private LocalDateTime order_date;
    private BigDecimal total_value;
    private String status;
    private LocalDateTime timestamp;

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

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public LocalDateTime getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    public BigDecimal getTotal_value() {
        return total_value;
    }

    public void setTotal_value(BigDecimal total_value) {
        this.total_value = total_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}