package com.example.technest.service;

import com.example.technest.entity.Cart;
import com.example.technest.entity.Users;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    public Users findUserByUsername(String username);
    public void addToCart(int userId, int productId, int quantity);
    public boolean isProductInCart(int userId, int productId);
    public int getCartCountByUsername(String username);
    public BigDecimal calculateSubTotal(List<Cart> cartItems);
    public void updateCartQuantity(int cartId, int quantity);
    public void removeCartItem(int cartId);
    public List<Cart> getCartItemsByUserId(int userId);
    void clearCartByUser(int userId);


}
