package com.example.technest.repo;

import com.example.technest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    List<Product> findByCategoryId(int categoryId);
    Optional<Product> findByProductCode(String productCode);
    List<Product> findByManufacturerAndCategoryId(String manufacturer, int categoryId);
}
