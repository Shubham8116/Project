package com.example.technest.service;

import com.example.technest.entity.Cart;
import com.example.technest.entity.Order;
import com.example.technest.entity.OrderItem;
import com.example.technest.entity.Users;

import java.util.List;

public interface OrderService {
    public Order placeOrder(Users appUser, String paymentMethod, List<Cart> cartItems);
    public List<OrderItem> getOrderItemsByOrderId(int orderId);
    public void cancelOrderById(int orderId);
}
