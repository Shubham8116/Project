package com.example.technest.service;

import com.example.technest.entity.Order;
import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import org.apache.catalina.User;

import java.util.List;

public interface UsersService {

    Users save(Users users);
    UsersDetails getUserDetailsByUsersId(int usersId);
    public List<Order> findOrdersByUserId(int userId);
    public void updatePersonalInfo(String username, String name, String phone);
    public void updateAddress(String username, String street, String city, String state, String pinCode);

}
