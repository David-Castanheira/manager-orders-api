package br.com.app.order.config.messaging.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.events.EventUpdateOrder;
import br.com.app.order.services.OrderService;

@Component
public class MessageConsumer {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "create-order-topic", groupId = "api-orders")
    public void consumeCreateOrderEvent(EventCreateOrder event) {
        System.out.println("Creation event requested successfully: " + event);
        try {
            orderService.processCreateOrderEvent(event);
        } catch (Exception e) {
            System.err.println("Error to process creation order event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "update-order-topic", groupId = "api-orders")
    public void consumeUpdateOrderEvent(EventUpdateOrder event) {
        System.out.println("Update event requested successfully: " + event);
        try {
            orderService.processUpdateOrderEvent(event);
        } catch (Exception e) {
            System.err.println("Error to process update order event: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "cancel-order-topic", groupId = "api-orders")
    public void consumeCancelOrderEvent(EventCancelOrder event) {
        System.out.println("Cancel event requested successfully: " + event);
        try {
            orderService.processCancelOrderEvent(event);
        } catch (Exception e) {
            System.err.println("Error to process cancel order event: " + e.getMessage());
        }
    }
}
