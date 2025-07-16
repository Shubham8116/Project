package com.example.technest.repo;

import com.example.technest.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory,Integer> {
    Inventory findByProductIdAndBatchNumber(int productID,int batchNumber);
    @Query("SELECT i FROM Inventory i WHERE i.product.category.id = :categoryId")
    List<Inventory> findByProductCategoryId(@Param("categoryId") int categoryId);
}

