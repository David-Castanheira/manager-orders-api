package br.com.app.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.order.models.EventOrderEntity;

@Repository
public interface EventOrderRepository extends JpaRepository<EventOrderEntity, Long> {
    List<EventOrderEntity> findByOrderId(Integer orderId);
}
