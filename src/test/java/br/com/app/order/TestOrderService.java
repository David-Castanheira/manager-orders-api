package br.com.app.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.models.Order;
import br.com.app.order.repositories.OrderRepository;
import br.com.app.order.services.OrderService;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestOrderService {

    @Spy
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Commit
    public void testCreateOrder() {
        EventCreateOrder event = new EventCreateOrder();
        event.setOrderId(3);
        event.setName("Teste");
        event.setOrder_date(LocalDateTime.now());
        event.setTotal_value(new BigDecimal("100.00"));
        event.setStatus("PENDING");
        event.setTimestamp(LocalDateTime.now());

        orderService.processCreateOrderEvent(event);

        Order found = entityManager.find(Order.class, event.getOrderId());
        assertNotNull(found);
        assertEquals("Teste", found.getName());
        verify(orderRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Order.class));
    }
}
