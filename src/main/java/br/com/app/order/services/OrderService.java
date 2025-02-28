package br.com.app.order.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.events.EventUpdateOrder;
import br.com.app.order.models.EventOrderEntity;
import br.com.app.order.models.Order;
import br.com.app.order.repositories.EventOrderRepository;
import br.com.app.order.repositories.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private EventOrderRepository eventOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventOrderService eventOrderService;

    public Order getOrder(Integer orderId) {
        System.out.println("Buscando pedido com orderId: " + orderId);
        List<EventOrderEntity> events = eventOrderRepository.findByOrderId(orderId);
        System.out.println("Eventos encontrados: " + events);
        return newOrderList(events);
    }

    public List<Order> getAllOrders() {
        List<EventOrderEntity> allEvents = eventOrderRepository.findAll();
        List<Order> orders = new ArrayList<>();
        List<Integer> orderIds = new ArrayList<>();

        for (EventOrderEntity event : allEvents) {
            if (!orderIds.contains(event.getOrderId())) {
                orderIds.add(event.getOrderId());
            }
        }

        for (Integer orderId : orderIds) {
            Order order = getOrder(orderId);
            if (order != null) {
                orders.add(order);
            }
        }

        return orders;
    }

    private Order newOrderList(List<EventOrderEntity> events) {
        if (events.isEmpty()) {
            return null;
        }

        Order order = new Order();
        boolean isCancelled = false;

        for (EventOrderEntity eventEntity : events) {
            String eventType = eventEntity.getEventType();
            String eventData = eventEntity.getEventData();

            if (eventType.equals("EventCreateOrder")) {
                EventCreateOrder createEvent = eventOrderService.desserializeEvent(eventData, EventCreateOrder.class);
                order.applicate(createEvent);
            } else if (eventType.equals("EventUpdateOrder")) {
                EventUpdateOrder updateEvent = eventOrderService.desserializeEvent(eventData, EventUpdateOrder.class);
                order.applicate(updateEvent);
            } else if (eventType.equals("EventCancelOrder")) {
                EventCancelOrder cancelEvent = eventOrderService.desserializeEvent(eventData, EventCancelOrder.class);
                isCancelled = true;
                order.setStatus("Order cancelled because a deletion was requested");
                orderRepository.deleteById(order.getOrderId());
                return null;
            }
        }

        return order;
    }
}
