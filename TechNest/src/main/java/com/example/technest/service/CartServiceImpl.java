package com.example.technest.service;

import com.example.technest.entity.Cart;
import com.example.technest.entity.Product;
import com.example.technest.entity.Users;
import com.example.technest.repo.CartRepo;
import com.example.technest.repo.ProductRepo;
import com.example.technest.repo.UsersRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private UsersRepo usersRepo;

    public Users findUserByUsername(String username) {
        return usersRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void addToCart(int userId, int productId, int quantity)
    {
        Product product = productRepo.findById(productId).orElse(null);
        Users user = usersRepo.findById(userId).orElse(null);


        if (product != null && user != null)
        {

            BigDecimal discount = product.getDiscount();
            BigDecimal discountPercentage = discount.divide(BigDecimal.valueOf(100));
//
            BigDecimal finalPrice = product.getPrice().subtract(product.getPrice().multiply(discountPercentage));

            BigDecimal itemTotalAmount = finalPrice.multiply(BigDecimal.valueOf(quantity));

            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQuantity(quantity);
            cart.setFinal_price(finalPrice);
            cart.setItemTotalAmount(itemTotalAmount);

            cartRepo.save(cart);
        }
    }

    @Override
    public boolean isProductInCart(int userId, int productId) {
        return cartRepo.existsByUserIdAndProductId(userId, productId);
    }

    public int getCartCountByUsername(String username) {
        //If we don't want to write .orElseThrow() then we need to use Optional<Users> user=
        Users user = usersRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Integer count = cartRepo.countByUserId(user.getId());
        return (count != null) ? count : 0;
    }



    public List<Cart> getCartItemsByUserId(int userId) {

        return cartRepo.findByUserId(userId);
    }


    @Override
    @Transactional
    public void clearCartByUser(int userId) {

        cartRepo.deleteByUserId(userId);
    }


    public void updateCartQuantity(int cartId, int quantity)
    {
    Optional<Cart> cartOptional = cartRepo.findById(cartId);
    if (cartOptional.isPresent())
        {
        Cart cart = cartOptional.get();
        cart.setQuantity(quantity);
        cart.setItemTotalAmount(cart.getFinal_price().multiply(BigDecimal.valueOf(quantity)));
        cartRepo.save(cart);
        }
    }


    @Override
    public void removeCartItem(int cartId)
    {
        cartRepo.deleteById(cartId);
    }



    public BigDecimal calculateSubTotal(List<Cart> cartItems)
    {
            return cartItems.stream()
            .map(Cart::getItemTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}


