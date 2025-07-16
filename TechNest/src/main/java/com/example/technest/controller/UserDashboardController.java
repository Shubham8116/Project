package com.example.technest.controller;

import com.example.technest.entity.Order;
import com.example.technest.entity.OrderItem;
import com.example.technest.entity.Users;
import com.example.technest.service.CartService;
import com.example.technest.service.OrderService;
import com.example.technest.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserDashboardController
{

    @Autowired
    private UsersService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/user")
    public String userDashboard(@AuthenticationPrincipal User user, Model model) {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        model.addAttribute("user", appUser);
        model.addAttribute("userDetails", appUser.getUsersDetails());

        List<Order> order=userService.findOrdersByUserId(appUser.getId());

        model.addAttribute("orders",order );
        return "user_dashboard";
    }

    @PostMapping("/update-info")
    public String updatePersonalInfo(@RequestParam String name, @RequestParam String phone, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes)
    {
        boolean hasError = false;

        if (!name.matches("^[a-zA-Z\\s]{1,30}$")) {
            redirectAttributes.addFlashAttribute("nameError", "Name must contain only letters and spaces, and be between 1 and 30 characters long");
            hasError = true;
        }

        if (!phone.matches("^\\d{10}$")) {
            redirectAttributes.addFlashAttribute("phoneError", "Phone must be exactly 10 digits");
            hasError = true;
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("invalidData", true);
            return "redirect:/user";
        }

        userService.updatePersonalInfo(user.getUsername(), name, phone);
        redirectAttributes.addFlashAttribute("infoUpdated", true);

        return "redirect:/user";
    }

    @PostMapping("/update-address")
    public String updateAddress(@RequestParam String street, @RequestParam String city, @RequestParam String state, @RequestParam String pinCode, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes)
    {

        boolean hasError = false;

        // Validate street (only letters and spaces)
        if (!street.matches("^[a-zA-Z0-9\\s,.]{1,30}$")) {
            redirectAttributes.addFlashAttribute("streetError", "Street must be between 1 to 30 characters long and may include letters, numbers, spaces, commas, or periods.");
            hasError = true;
        }

        // Validate city
        if (!city.matches("^[a-zA-Z\\s]{1,20}$")) {
            redirectAttributes.addFlashAttribute("cityError", "City must contain only letters and spaces, and be between 1 and 20 characters long");
            hasError = true;
        }

        // Validate state
        if (!state.matches("^[A-Za-z ]+$")) {
            redirectAttributes.addFlashAttribute("stateError", "State must contain only letters");
            hasError = true;
        }

        // Validate pin code (exactly 6 digits)
        if (!pinCode.matches("^\\d{6}$")) {
            redirectAttributes.addFlashAttribute("pinError", "Pin Code must be exactly 6 digits");
            hasError = true;
        }

        if (hasError) {
            redirectAttributes.addFlashAttribute("invalidAddressData", true); // Section flag
            return "redirect:/user";
        }


        userService.updateAddress(user.getUsername(), street, city, state, pinCode);
        redirectAttributes.addFlashAttribute("infoUpdated", true);

        return "redirect:/user";
    }

    @GetMapping("/order-items")
    public String getOrderItems(@RequestParam("orderId") int orderId, Model model) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderId);
        model.addAttribute("orderItems", orderItems);
        return "/fragments/order_items:: orderDetails" ; // points to Thymeleaf fragment
    }

    @PostMapping("/cancel-order")
    @ResponseBody
    public String cancelOrder(@RequestParam("orderId") int orderId) {
        orderService.cancelOrderById(orderId); // logic to update status in DB
        return "Order cancelled successfully";
    }

}
