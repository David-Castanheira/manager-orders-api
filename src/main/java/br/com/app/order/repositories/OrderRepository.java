package br.com.app.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.app.order.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
}
