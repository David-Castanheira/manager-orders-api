package br.com.app.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.app.order.models.EventOrderEntity;
import br.com.app.order.models.Order;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestCreateOrderController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Commit
    public void testCreateOrderSavedEventToDatabase() throws Exception {
        Order order = new Order();
        order.setName("Test Order");
        order.setOrder_date(LocalDateTime.now());
        order.setTotal_value(new BigDecimal("40.00"));
        order.setStatus("Confirmado");
        order.setTimestamp(LocalDateTime.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        EventOrderEntity savedEvent = entityManager.find(EventOrderEntity.class, 1L);
        assertNotNull(savedEvent);
        assertEquals("EventCreateOrder", savedEvent.getEventType());
    }
}
