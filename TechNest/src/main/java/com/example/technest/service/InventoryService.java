package com.example.technest.service;

import com.example.technest.dto.InventoryFormDto;
import com.example.technest.entity.Inventory;

import java.util.List;

public interface InventoryService {

    public void deleteInventoryById(int id);
    List<Inventory> getInventoryByCategoryId(int categoryId);
    public void saveInventoryWithProduct(InventoryFormDto dto);
    public Inventory getInventoryById(int id);
    public InventoryFormDto convertToDto(Inventory inventory);
    public void updateInventory(InventoryFormDto dto);


}
