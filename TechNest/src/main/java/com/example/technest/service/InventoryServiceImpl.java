package com.example.technest.service;

import com.example.technest.dto.InventoryFormDto;
import com.example.technest.entity.Inventory;
import com.example.technest.entity.Product;
import com.example.technest.exception.ProductNotFoundException;
import com.example.technest.repo.InventoryRepo;
import com.example.technest.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public void deleteInventoryById(int id) {
        inventoryRepo.deleteById(id);
    }

    @Override
    public List<Inventory> getInventoryByCategoryId(int categoryId)
    {
     return  inventoryRepo.findByProductCategoryId(categoryId);
    }


    @Override
    public void saveInventoryWithProduct(InventoryFormDto dto) {
        //  Look for product using product code
        Optional<Product> optionalProduct = productRepo.findByProductCode(dto.getProductCode().trim().toUpperCase());

        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Product not found with code: " + dto.getProductCode());
        }

        Product product = optionalProduct.get();
        // Map DTO to Inventory entity
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setStockAvailable(dto.getStockAvailable());
        inventory.setWarehouseLocation(dto.getWarehouseLocation());
        inventory.setBatchNumber(dto.getBatchNumber());
        inventory.setSupplier(dto.getSupplier());
        inventory.setReceivedDate(dto.getReceivedDate());

        //  Save to repository
        inventoryRepo.save(inventory);
    }

    @Override
    public Inventory getInventoryById(int id) {
        return inventoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + id));

    }

    @Override
    public InventoryFormDto convertToDto(Inventory inventory) {
        InventoryFormDto dto = new InventoryFormDto();


        dto.setId(inventory.getId());
        dto.setProductCode(inventory.getProduct().getProductCode());
        dto.setStockAvailable(inventory.getStockAvailable());
        dto.setWarehouseLocation(inventory.getWarehouseLocation());
        dto.setBatchNumber(inventory.getBatchNumber());
        dto.setSupplier(inventory.getSupplier());
        dto.setReceivedDate(inventory.getReceivedDate());

        return dto;
    }


    @Override
    public void updateInventory(InventoryFormDto dto) {
        //  Fetch existing inventory

        Inventory inventory = inventoryRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + dto.getId()));

        inventory.setStockAvailable(dto.getStockAvailable());
        inventory.setWarehouseLocation(dto.getWarehouseLocation());
        inventory.setBatchNumber(dto.getBatchNumber());
        inventory.setSupplier(dto.getSupplier());
        inventory.setReceivedDate(dto.getReceivedDate());

        //  Save updated entity
        inventoryRepo.save(inventory);
    }

}
