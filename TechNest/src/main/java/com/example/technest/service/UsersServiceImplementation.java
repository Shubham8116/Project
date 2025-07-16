package com.example.technest.service;

import com.example.technest.entity.Order;
import com.example.technest.entity.Role;
import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import com.example.technest.repo.OrderRepo;
import com.example.technest.repo.RoleRepo;
import com.example.technest.repo.UsersDetailsRepo;
import com.example.technest.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class UsersServiceImplementation implements UsersService {

    @Autowired
    public final UsersRepo usersRepo;
    @Autowired
    public final UsersDetailsRepo usersDetailsRepo;
    @Autowired
    public final RoleRepo roleRepo;// we have written public final therefore we need to use constructor to initialize them
    @Autowired
    private OrderRepo orderRepo;// here we use private therefore we don;t need to initialize it.


    public UsersServiceImplementation(UsersRepo usersRepo, UsersDetailsRepo usersDetailsRepo, RoleRepo roleRepo) {
        this.usersRepo = usersRepo;
        this.usersDetailsRepo = usersDetailsRepo;
        this.roleRepo = roleRepo;
    }


    @Override
    @Transactional
    public Users save(Users users) {

        HashSet<Role> rolesSet = new HashSet<>();
        Role roles = roleRepo.findByRoleName("ROLE_USER");
        rolesSet.add(roles);
        users.setRoles(rolesSet);

        // Save UsersDetails first then users
        //1) get users details from user and save in details
        UsersDetails details = users.getUsersDetails();

        //2)now we have to set users which we have created in UserDetails entity
        details.setUsers(users); // Ensure the bidirectional relationship is set

        //3) now save userdetails using JPA repo
        usersDetailsRepo.save(details);

        // 4) now we have to set userdetails of users class which we have created in Users entity
        users.setUsersDetails(details); // This line ensures the relationship is maintained

        // 5) now save users using JPA repo
        return usersRepo.save(users);

    }

    @Override
    public UsersDetails getUserDetailsByUsersId(int usersId) {
        return usersDetailsRepo.findByUsersId(usersId).orElse(null);
    }

    @Transactional
    public List<Order> findOrdersByUserId(int userId)
    {
        List<Order> orders = orderRepo.findByUserId(userId);
        Date today = new Date();

        for (Order order : orders) {
        if (order.getOrderStatus() != Order.OrderStatus.DELIVERED &&
                order.getOrderStatus() != Order.OrderStatus.CANCELLED &&
                order.getEstimatedDeliveryDate() != null &&
                order.getEstimatedDeliveryDate().before(today)) {

            order.setOrderStatus(Order.OrderStatus.DELIVERED);
            orderRepo.save(order);
        }
    }

        return orders;

    }

    @Transactional
    public void updatePersonalInfo(String username, String name, String phone) {

        Users user = usersRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user != null) {
            user.setName(name);

            UsersDetails details = user.getUsersDetails();
            if (details != null) {
                details.setPhone(phone);
            }

            usersRepo.save(user); // Cascade will save UsersDetails too
        }

    }

    @Transactional
    public void updateAddress(String username, String street, String city, String state, String pinCode)
    {
        Users user =usersRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        UsersDetails userDetails = user.getUsersDetails();
        userDetails.setStreet(street);
        userDetails.setCity(city);
        userDetails.setState(state);
        userDetails.setPinCode(pinCode);
        usersRepo.save(user);
    }

}
