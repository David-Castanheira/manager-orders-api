package br.com.app.order.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.models.Order;
import br.com.app.order.repositories.OrderRepository;
import br.com.app.order.services.EventOrderService;
import br.com.app.order.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private EventOrderService eventOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Integer id) {
        Order order = orderService.getOrder(id);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        order.setTimestamp(LocalDateTime.now());

        // Persiste o pedido primeiro para obter o orderId gerado
        Order orderSaved = orderRepository.save(order);

        // Cria o evento com o orderId gerado
        eventOrderService.saveEventOrder(orderSaved.toCreateEvent());

        // Recupera o pedido criado a partir dos eventos
        Order orderCreated = orderService.getOrder(orderSaved.getOrderId());

        return ResponseEntity.status(HttpStatus.CREATED).body(orderCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order orderUpdated) {
        orderUpdated.setOrderId(id);
        orderUpdated.setTimestamp(LocalDateTime.now());
        eventOrderService.updateEventOrder(orderUpdated.toUpdateEvent());
        
        Order orderUpdatedRes = orderService.getOrder(id);
        if (orderUpdatedRes != null) {
            orderRepository.save(orderUpdated); 
            return ResponseEntity.ok(orderUpdatedRes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        EventCancelOrder cancelOrder = new EventCancelOrder(id, LocalDateTime.now());
        eventOrderService.cancelEventOrder(cancelOrder);
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build(); 
    }
}

