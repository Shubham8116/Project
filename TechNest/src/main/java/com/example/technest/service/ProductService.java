package com.example.technest.service;

import com.example.technest.dto.ProductFormDto;
import com.example.technest.entity.Product;
import com.example.technest.entity.Users;

import java.util.List;

public interface ProductService {
    public List<Product> getProductsByCategoryId(int categoryId);
    public Product getProductDetailsByProductId(int productId);
    public void saveProductWithInventory(ProductFormDto dto);
    public void deleteProductById(int id);
    public Product getProductById(int id);
    public ProductFormDto convertToDto(Product product);
    public void updateProduct(ProductFormDto dto);
    public List<Product> getProductsWithCalculatedPrice(int categoryId);
    public List<Product> getProductsByManufacturerAndCategoryId(String manufacturer, int categoryId);




}

