package br.com.app.order.config.messaging.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.app.order.events.EventCreateOrder;

@Service
public class CreateOrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(EventCreateOrder event) {
        kafkaTemplate.send("create-order-topic", event);
    }
}
