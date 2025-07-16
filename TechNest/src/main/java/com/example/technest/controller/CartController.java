package com.example.technest.controller;

import com.example.technest.entity.Cart;
import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import com.example.technest.service.CartService;
import com.example.technest.service.ProductService;
import com.example.technest.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @Autowired
    private UsersService usersService;



    @PostMapping("/add-to-cart")
    @ResponseBody
    public String addToCart(@RequestParam("productId") int productId,
                            @AuthenticationPrincipal org.springframework.security.core.userdetails.User user,
                            Model model) {

        // Get user from authentication


        Users appUser = cartService.findUserByUsername(user.getUsername());

        cartService.addToCart(appUser.getId(), productId, 1);


        return "Product added to cart successfully!";
    }


    @GetMapping("/check-cart")
    @ResponseBody
    public Map<String, Boolean> checkCart(@RequestParam("productId") int productId,
                                          @AuthenticationPrincipal org.springframework.security.core.userdetails.User user) {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        boolean inCart = cartService.isProductInCart(appUser.getId(), productId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("inCart", inCart);


        return response;
    }

    @GetMapping("/count")
    @ResponseBody
    public int getCartCount(Principal principal) {
        String username = principal.getName();
        return cartService.getCartCountByUsername(username);
    }

    @GetMapping("/view-cart")
    public String viewCart(@AuthenticationPrincipal User user, Model model) {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        List<Cart> cartItems = cartService.getCartItemsByUserId(appUser.getId());

        BigDecimal subTotal = cartService.calculateSubTotal(cartItems).setScale(0, BigDecimal.ROUND_HALF_UP);;
        BigDecimal gst = subTotal.multiply(BigDecimal.valueOf(0.18)).setScale(0, BigDecimal.ROUND_HALF_UP);;
        BigDecimal grandTotal = subTotal.add(gst).setScale(0, BigDecimal.ROUND_HALF_UP);;

        for (Cart item : cartItems) {
            item.setItemTotalAmount(item.getItemTotalAmount().setScale(0, RoundingMode.HALF_UP));
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("gst", gst);
        model.addAttribute("grandTotal", grandTotal);

        return "cart";
    }


    @PostMapping("/update-cart")
    @ResponseBody
    public String updateCart(@RequestParam("cartId") int cartId, @RequestParam("quantity") int quantity) {
        cartService.updateCartQuantity(cartId, quantity);

        return "Cart updated successfully!";
    }

    @PostMapping("/remove-from-cart")
    public ResponseEntity<String> removeFromCart(@RequestParam("cartId") int cartId) {
        cartService.removeCartItem(cartId);
        return ResponseEntity.ok("Item removed successfully");
    }


    @GetMapping("/checkout")
    public String proceedCheckout(@AuthenticationPrincipal User user,Model model)
    {
        Users appUser = cartService.findUserByUsername(user.getUsername());
        UsersDetails userDetails = usersService.getUserDetailsByUsersId(appUser.getId());

        model.addAttribute("user", appUser);
        model.addAttribute("userDetails", userDetails);
        return "address_payment_details";
    }
}