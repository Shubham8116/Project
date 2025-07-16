package com.example.technest.repo;

import com.example.technest.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Integer> {
   Optional <Category> findByName(String name);
}
