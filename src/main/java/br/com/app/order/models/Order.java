package br.com.app.order.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.events.EventUpdateOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Integer orderId;

    @Column(name = "name", length = 255, nullable = false)
    private String name; 

    @Column(name = "order_date")
    private LocalDateTime order_date;

    @Column(name = "total_value")
    private BigDecimal total_value;

    @Column(name = "status", length = 255, nullable = false)
    private String status;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public Order() {

    }

    public void applicate(EventCreateOrder event) {
        this.orderId = event.getOrderId();
        this.name = event.getName();
        this.order_date = event.getOrder_date();
        this.total_value = event.getTotal_value();
        this.status = event.getStatus();
        this.timestamp = event.getTimestamp();
    }

    public void applicate(EventUpdateOrder event) {
        this.orderId = event.getOrderId();
        this.name = event.getName();
        this.order_date = event.getOrder_date();
        this.total_value = event.getTotal_value();
        this.status = event.getStatus();
        this.timestamp = event.getTimestamp();
    }

    public EventCreateOrder toCreateEvent() {
        return new EventCreateOrder(this.orderId, this.name, this.order_date, this.total_value, this.status, this.timestamp);
    }

    public EventUpdateOrder toUpdateEvent() {
        return new EventUpdateOrder(this.orderId, this.name, this.order_date, this.total_value, this.status, this.timestamp);
    }

    public static EventCancelOrder toCancelEvent(Integer orderId, LocalDateTime timestamp) {
        return new EventCancelOrder(orderId, timestamp);
    }

    public Order(String name, LocalDateTime order_date, BigDecimal total_value, String status, LocalDateTime timestamp) {
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
