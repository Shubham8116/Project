package com.example.technest.repo;

import com.example.technest.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderId(int orderId);

}
