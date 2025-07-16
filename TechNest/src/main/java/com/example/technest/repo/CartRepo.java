package com.example.technest.repo;

import com.example.technest.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart,Integer> {
    boolean existsByUserIdAndProductId(int userId, int productId);

    @Query("SELECT SUM(c.quantity) FROM Cart c WHERE c.user.id = :userId")
    Integer countByUserId(@Param("userId") int userId);

    List<Cart> findByUserId(int userId);

    void deleteByUserId(int userId);


}
