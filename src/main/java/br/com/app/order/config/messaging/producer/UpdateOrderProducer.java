package br.com.app.order.config.messaging.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.app.order.events.EventUpdateOrder;

@Service
public class UpdateOrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(EventUpdateOrder event) {
        kafkaTemplate.send("update-order-topic", event);
    }
}
