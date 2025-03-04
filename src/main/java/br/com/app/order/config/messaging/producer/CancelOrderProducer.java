package br.com.app.order.config.messaging.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.app.order.events.EventCancelOrder;

@Service
public class CancelOrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(EventCancelOrder event) {
        kafkaTemplate.send("cancel-order-topic", event);
    }
}
