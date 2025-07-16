package com.example.technest.repo;

import com.example.technest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    Role findByRoleName(String name);

}
