package com.example.technest.service;

import com.example.technest.entity.*;
import com.example.technest.repo.InventoryRepo;
import com.example.technest.repo.OrderItemRepo;
import com.example.technest.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.*;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    OrderItemRepo orderItemRepo;

    @Autowired
    InventoryRepo inventoryRepo;

    @Override
    @Transactional
    public Order placeOrder(Users appUser, String paymentMethod, List<Cart> cartItems) {

        UsersDetails userDetails = usersService.getUserDetailsByUsersId(appUser.getId());
        //Calculate order total
        BigDecimal subTotal = cartService.calculateSubTotal(cartItems);
        BigDecimal gst = subTotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal grandTotal = subTotal.add(gst).setScale(0, BigDecimal.ROUND_HALF_UP);


        Order order = new Order();
        order.setOrderNumber(generateRandomOrderNumber());
        order.setUser(appUser);
        order.setTotalAmount(grandTotal);
        order.setOrderStatus(Order.OrderStatus.CONFIRMED);
        order.setShippingAddress(formatShippingAddress(userDetails));
        order.setPaymentMethod(paymentMethod);
        order.setTrackingNumber(generateUniqueTrackingNumber());
        order.setShipmentDate(new Date());
        order.setEstimatedDeliveryDate(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        orderRepo.save(order);

        // Create OrderItems and update Inventory
        for (Cart cartItem : cartItems)
        {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setItemTotalAmount(cartItem.getItemTotalAmount());
            orderItemRepo.save(orderItem);

            // Update Inventory
            //currently we are accessing only batch number 101.
            Inventory inventory = inventoryRepo.findByProductIdAndBatchNumber(product.getId(),101);

            if (inventory != null)
            {

                inventory.setStockAvailable(inventory.getStockAvailable() - cartItem.getQuantity());

                inventoryRepo.save(inventory);

            }

        }
     return order;

    }

    private String generateRandomOrderNumber()
    {
        Random random = new SecureRandom();
        return String.format("%04d", random.nextInt(10000));
    }

    private String formatShippingAddress(UsersDetails userDetails)
    {
        return userDetails.getStreet() + ", " + userDetails.getCity() + ", " + userDetails.getState() + ", " + userDetails.getPinCode();
    }

    private String generateUniqueTrackingNumber()
    {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId)
    {
        return orderItemRepo.findByOrderId(orderId);
    }

    public void cancelOrderById(int orderId) {
        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setOrderStatus(Order.OrderStatus.CANCELLED);
            order.setModifiedAt(new Timestamp(System.currentTimeMillis()));
            orderRepo.save(order);
        }
    }



}
