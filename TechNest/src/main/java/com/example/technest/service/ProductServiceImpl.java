package com.example.technest.service;

import com.example.technest.constant.ConstantData;
import com.example.technest.dto.ProductFormDto;
import com.example.technest.entity.*;
import com.example.technest.repo.CategoryRepo;
import com.example.technest.repo.InventoryRepo;
import com.example.technest.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private InventoryRepo inventoryRepo;



    public List<Product> getProductsWithCalculatedPrice(int categoryId) {
        List<Product> products = getProductsByCategoryId(categoryId);
        for (Product product : products) {
            if (product.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal price = product.getPrice();
                BigDecimal discount = price.multiply(product.getDiscount()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = price.subtract(discount).setScale(0, RoundingMode.HALF_UP);
                product.setFinalPrice(finalPrice.doubleValue());
            } else {
                product.setFinalPrice(product.getPrice().doubleValue());
            }
        }
        return products;
    }




    @Override
    public List<Product> getProductsByCategoryId(int categoryId)
    {

        return productRepo.findByCategoryId(categoryId);

    }

    @Override
    public Product getProductDetailsByProductId(int productId)
    {
        return productRepo.findById(productId).orElseThrow(()->new RuntimeException("product not found with ID:"+ productId));
    }

    @Override
    public void saveProductWithInventory(ProductFormDto dto)
    {
        int categoryId = getCategoryIdByName(dto.getCategoryName());
        Category category = categoryRepo.findById(categoryId).orElseThrow();

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setUnit(dto.getUnit());
        product.setPrice(dto.getPrice());
        product.setUrl(dto.getUrl());
        product.setStatus(Status.available);
        product.setDiscount(dto.getDiscount());
        product.setManufacturer(dto.getManufacturer());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
        product.setProductCode(dto.getProductCode());
        product.setCategory(category);

        Product savedProduct = productRepo.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(savedProduct);
        // we have first write : Product savedProduct = productRepo.save(product); because inventory table has foreign id for product
        // so to generate product id we first stored product then that savedProduct is set in inventory
        // that is why we dont write : inventory.setProduct(product); becuse the new id is not generated.
        inventory.setStockAvailable(dto.getStockAvailable());
        inventory.setWarehouseLocation(dto.getWarehouseLocation());
        inventory.setBatchNumber(dto.getBatchNumber());
        inventory.setSupplier(dto.getSupplier());
        inventory.setReceivedDate(dto.getReceivedDate());

        inventoryRepo.save(inventory);



    }

    @Override
    @Transactional
    public void deleteProductById(int id)
    {
     Product product=productRepo.findById(id).orElseThrow(()->new RuntimeException("Product not found with given Id"));
        // Delete all inventories linked to this product
        List<Inventory> inventoryList = product.getInventoryList();
        if (inventoryList != null && !inventoryList.isEmpty()) {
            inventoryRepo.deleteAll(inventoryList);
        }
        // Now delete the product itself
        productRepo.delete(product);



    }

    private int getCategoryIdByName(String name) {
        switch (name.toLowerCase()) {
            case "television" :return ConstantData.televisionID;
            case "washing machine" :return ConstantData.washingMachineID;
            case "water purifier" :return ConstantData.waterPurifierID;
            case "geyser" :return ConstantData.geyserID;
            case "ac" :return ConstantData.airConditionerID;
            case "refrigerator" :return ConstantData.refrigeratorId;
            default : throw new IllegalArgumentException("Invalid category: " + name);
        }
    }

    public Product getProductById(int id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    public ProductFormDto convertToDto(Product product) {
        ProductFormDto dto = new ProductFormDto();

        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setUnit(product.getUnit());
        dto.setPrice(product.getPrice());
        dto.setUrl(product.getUrl());
        dto.setDiscount(product.getDiscount());
        dto.setManufacturer(product.getManufacturer());
        dto.setWarrantyPeriod(product.getWarrantyPeriod());
        dto.setProductCode(product.getProductCode());
        dto.setCategoryName(product.getCategory().getName());

        // Skipping inventory fields during update
        return dto;
    }


    public void updateProduct(ProductFormDto dto) {
        Product existing = productRepo.findByProductCode(dto.getProductCode()).orElseThrow(() -> new RuntimeException("Product not found with code: "));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setUnit(dto.getUnit());
        existing.setPrice(dto.getPrice());
        existing.setUrl(dto.getUrl());
        existing.setDiscount(dto.getDiscount());
        existing.setManufacturer(dto.getManufacturer());
        existing.setWarrantyPeriod(dto.getWarrantyPeriod());

        Category category = categoryRepo.findByName(dto.getCategoryName()).orElseThrow(()->new RuntimeException("Category not found"));
        existing.setCategory(category);

        productRepo.save(existing);
    }





    @Override
    public List<Product> getProductsByManufacturerAndCategoryId(String manufacturer, int categoryId) {

        List<Product> products = productRepo.findByManufacturerAndCategoryId(manufacturer, categoryId);;
        for (Product product : products) {
            if (product.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal price = product.getPrice();
                BigDecimal discount = price.multiply(product.getDiscount()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = price.subtract(discount).setScale(0, RoundingMode.HALF_UP);
                product.setFinalPrice(finalPrice.doubleValue());
            } else {
                product.setFinalPrice(product.getPrice().doubleValue());
            }
        }
        return products;

    }


}


