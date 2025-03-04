package br.com.app.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import br.com.app.order.models.EventOrderEntity;
import br.com.app.order.repositories.EventOrderRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventOrderRepository eventOrderRepository;

    @Test
    @Commit
    public void testSaveEventOrder() {
        EventOrderEntity event = new EventOrderEntity();
        event.setOrderId(1);
        event.setEventType("TestEvent");
        event.setEventData("TestData");

        EventOrderEntity savedEvent = eventOrderRepository.save(event);

        assertNotNull(savedEvent.getEventId());
        assertEquals(1, savedEvent.getOrderId());
        assertEquals("TestEvent", savedEvent.getEventType());
        assertEquals("TestData", savedEvent.getEventData());

        EventOrderEntity foundEvent = entityManager.find(EventOrderEntity.class, savedEvent.getEventId());
        assertNotNull(foundEvent);
        assertEquals(savedEvent.getEventId(), foundEvent.getEventId());
    }
}
