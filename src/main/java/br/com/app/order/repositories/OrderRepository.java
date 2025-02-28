package br.com.app.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.order.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
    
}
