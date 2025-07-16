package com.example.technest.repo;

import com.example.technest.entity.UsersDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersDetailsRepo extends JpaRepository<UsersDetails,Integer> {
    Optional<UsersDetails> findByUsersId(int usersId);
    void deleteByUsersId(int usersId);
}
