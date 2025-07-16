package com.example.technest.service;

import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import com.example.technest.repo.UsersDetailsRepo;
import com.example.technest.repo.UsersRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private final UsersRepo usersRepo;
    private final UsersDetailsRepo usersDetailsRepo;

    public AdminServiceImpl(UsersRepo usersRepo, UsersDetailsRepo usersDetailsRepo) {
        this.usersRepo = usersRepo;
        this.usersDetailsRepo = usersDetailsRepo;
    }

    @Override
    public Page<Users> getUsers(int page, int size) {

        return usersRepo.findAll(PageRequest.of(page, size));
    }


    @Override
    public UsersDetails getUserDetailsByUsersId(int usersId) {
        return usersDetailsRepo.findByUsersId(usersId).orElse(null);
    }



    @Override
    @Transactional
    public void deleteUserAndDetails(int userId) {
        // Step 1: Fetch the user

        Users user= usersRepo.findById(userId).orElseThrow(()->new RuntimeException("user not found with ID:"+ userId));

        // Step 2: Clear user-role association, we have to delete entry from users_role table
        user.getRoles().clear();
        usersRepo.save(user);

        // Step 3: Delete user details first
        usersDetailsRepo.deleteByUsersId(userId);

        // Step 4: Then delete the user
        usersRepo.delete(user);

    }
}
