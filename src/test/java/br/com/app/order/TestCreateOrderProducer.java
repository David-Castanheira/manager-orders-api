package br.com.app.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import br.com.app.order.config.messaging.producer.CreateOrderProducer;
import br.com.app.order.events.EventCreateOrder;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class TestCreateOrderProducer {

    @Autowired
    private CreateOrderProducer createOrderProducer;

    @Test
    void testSend() {
        EventCreateOrder event = new EventCreateOrder();
        // Passando os parâmetros na requisição para teste de envio e mensagens
        event.setOrderId(1);
        event.setName("Test Order");
        event.setOrder_date(LocalDateTime.now());
        event.setTotal_value(new BigDecimal(100.00));
        event.setStatus("Pending");
        event.setTimestamp(LocalDateTime.now());

        assertDoesNotThrow(() -> createOrderProducer.send(event));
    }
}
