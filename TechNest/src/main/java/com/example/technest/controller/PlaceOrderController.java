package com.example.technest.controller;

import com.example.technest.entity.Cart;
import com.example.technest.entity.Order;
import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import com.example.technest.service.CartService;
import com.example.technest.service.OrderService;
import com.example.technest.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; import java.util.List;

@Controller
public class PlaceOrderController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/place-order")
    public String showPlaceOrderPage(@AuthenticationPrincipal User user, @RequestParam String paymentMethod, Model model)
    {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        UsersDetails userDetails = usersService.getUserDetailsByUsersId(appUser.getId());
        List<Cart> cartItems = cartService.getCartItemsByUserId(appUser.getId());

        BigDecimal subTotal = cartService.calculateSubTotal(cartItems);
        BigDecimal gst = subTotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal grandTotal = subTotal.add(gst).setScale(0, BigDecimal.ROUND_HALF_UP);

        LocalDate arrivalDate = LocalDate.now().plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");


        for (Cart item : cartItems) {
            item.setItemTotalAmount(item.getItemTotalAmount().setScale(0, RoundingMode.HALF_UP));
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("arrivalDate", arrivalDate.format(formatter));
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("paymentMethod", paymentMethod);

        return "place_order";

    }

    @GetMapping("/order-success")
    public String showOrderSuccessFinalPage(@AuthenticationPrincipal User user, @RequestParam String paymentMethod,Model model)
    {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        List<Cart> cartItems = cartService.getCartItemsByUserId(appUser.getId());
        Order order=orderService.placeOrder(appUser, paymentMethod, cartItems);
        // Clear user's cart after order placement
         cartService.clearCartByUser(appUser.getId());
        model.addAttribute("order", order);
        return "order_success";
    }
}
