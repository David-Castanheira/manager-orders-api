package br.com.app.order.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.order.config.messaging.producer.CancelOrderProducer;
import br.com.app.order.config.messaging.producer.CreateOrderProducer;
import br.com.app.order.config.messaging.producer.UpdateOrderProducer;
import br.com.app.order.events.EventCancelOrder;
import br.com.app.order.exception.OrderNotFoundException;
import br.com.app.order.models.Order;
import br.com.app.order.repositories.OrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "RESTful API for orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CreateOrderProducer createOrderProducer;

    @Autowired
    private UpdateOrderProducer updateOrderProducer;

    @Autowired
    private CancelOrderProducer cancelOrderProducer;

    @GetMapping
    @Operation(summary = "List all orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "List an order based in ID")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Create an order")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        try {
            // Salva o objeto Order no banco para gerar o id, antes de invocar o método de criar o evento
            order = orderRepository.save(order);
            // Salva novamente no banco porém já com o id gerado
            createOrderProducer.send(order.toCreateEvent());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a existing order based on a ID")
    public ResponseEntity<Void> updateOrder(@PathVariable Integer id, @Valid @RequestBody Order order) {
        try {
            order.setOrderId(id);
            updateOrderProducer.send(order.toUpdateEvent());
            return ResponseEntity.ok().build();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a order based on ID")
    public ResponseEntity<Void> cancelOrder(@PathVariable Integer id) {
        try {
            EventCancelOrder event = new EventCancelOrder();
            event.setOrderId(id);
            event.setTimestamp(LocalDateTime.now());
            cancelOrderProducer.send(event);
            return ResponseEntity.ok().build();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
