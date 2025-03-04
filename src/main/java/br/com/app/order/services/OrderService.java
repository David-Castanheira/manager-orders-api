package br.com.app.order.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.events.EventCreateOrder;
import br.com.app.order.events.EventUpdateOrder;
import br.com.app.order.exception.EmptyEventsException;
import br.com.app.order.exception.ProcessCancelOrderNotFound;
import br.com.app.order.exception.ProcessUpdateOrderNotFound;
import br.com.app.order.models.EventOrderEntity;
import br.com.app.order.models.Order;
import br.com.app.order.repositories.EventOrderRepository;
import br.com.app.order.repositories.OrderRepository;
import jakarta.transaction.Transactional;

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
            throw new EmptyEventsException("The list of events cannot be empty");
        }

        Order order = new Order();
        boolean isCancelled = false;

        for (EventOrderEntity eventEntity : events) {
            String eventType = eventEntity.getEventType();
            String eventData = eventEntity.getEventData();

            if (eventType.equals("EventCreateOrder")) {
                EventCreateOrder createEvent = eventOrderService.deserializeEvent(eventData, EventCreateOrder.class);
                order.applicate(createEvent);
            } else if (eventType.equals("EventUpdateOrder")) {
                EventUpdateOrder updateEvent = eventOrderService.deserializeEvent(eventData, EventUpdateOrder.class);
                order.applicate(updateEvent);
            } else if (eventType.equals("EventCancelOrder")) {
                EventCancelOrder cancelEvent = eventOrderService.deserializeEvent(eventData, EventCancelOrder.class);
                isCancelled = true;
                order.setStatus("cancelled");
                orderRepository.deleteById(order.getOrderId());
            }
        }

        return order;
    }

    @Transactional
    public void processCreateOrderEvent(EventCreateOrder event) {
            System.out.println("Order creation event is being processed: " + event);
            Order order = new Order();
            order.setOrderId(event.getOrderId());
            order.setName(event.getName());
            order.setOrder_date(event.getOrder_date());
            order.setTotal_value(event.getTotal_value());
            order.setStatus(event.getStatus());
            order.setTimestamp(LocalDateTime.now());
            orderRepository.save(order);

            // Salvar o evento na tabela 'tbl_events_order'
            EventOrderEntity eventEntity = new EventOrderEntity();
            eventEntity.setOrderId(event.getOrderId());
            eventEntity.setEventType("EventCreateOrder");
            eventEntity.setEventData(eventOrderService.serializeEvent(event));
            eventEntity.setTimestamp(LocalDateTime.now());
            eventOrderRepository.save(eventEntity);
        }

    @Transactional
    public void processUpdateOrderEvent(EventUpdateOrder event) {
        System.out.println("Order update event is being processed: " + event);
            Order order = orderRepository.findById(event.getOrderId()).orElse(null);
            if (order != null) {
                order.setOrderId(event.getOrderId());
                order.setName(event.getName());
                order.setOrder_date(event.getOrder_date());
                order.setTotal_value(event.getTotal_value());
                order.setStatus(event.getStatus());
                order.setTimestamp(LocalDateTime.now());
                orderRepository.save(order);

                EventOrderEntity eventEntity = new EventOrderEntity();
                eventEntity.setOrderId(event.getOrderId());
                eventEntity.setEventType("EventUpdateOrder");
                eventEntity.setEventData(eventOrderService.serializeEvent(event));
                eventEntity.setTimestamp(LocalDateTime.now());
                eventOrderRepository.save(eventEntity);

            } else {
                throw new ProcessUpdateOrderNotFound("Event not updated because not found: " + event.getOrderId());
            }
        }

    @Transactional
    public void processCancelOrderEvent(EventCancelOrder event) {
        System.out.println("Order cancellation event is being processed: " + event);
            Order order = orderRepository.findById(event.getOrderId()).orElse(null);
            if (order != null) {
                order.setOrderId(event.getOrderId());
                order.setTimestamp(event.getTimestamp());
                orderRepository.delete(order);

                EventOrderEntity eventEntity = new EventOrderEntity();
                eventEntity.setOrderId(event.getOrderId());
                eventEntity.setEventType("EventCancelOrder");
                eventEntity.setEventData(eventOrderService.serializeEvent(event));
                eventEntity.setTimestamp(LocalDateTime.now());
                eventOrderRepository.save(eventEntity);
        } else {
            throw new ProcessCancelOrderNotFound("Event not cancelled because not found: " + event.getOrderId());
        }
    }
}
