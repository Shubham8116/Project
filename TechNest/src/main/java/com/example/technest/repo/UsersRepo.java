package com.example.technest.repo;

import com.example.technest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmail(String username);
}
