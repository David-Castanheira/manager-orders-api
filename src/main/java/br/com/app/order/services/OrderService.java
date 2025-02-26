package br.com.app.order.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.app.order.models.Order;
import br.com.app.order.repositories.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return (List<Order>) orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Integer id, Order updateOrder) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if(existingOrder.isPresent()) {
            updateOrder.setOrderId(id);
            return orderRepository.save(updateOrder);
        } else {
            return null;  
        }
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }
}
