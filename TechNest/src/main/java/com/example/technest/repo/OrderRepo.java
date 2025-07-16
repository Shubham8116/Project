package com.example.technest.repo;


import com.example.technest.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order,Integer> {
     List<Order> findByUserId(int userId);
}
