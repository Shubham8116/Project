package com.example.technest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class InventoryFormDto {

    private Integer id;

    @NotBlank(message = "Product code is required")
    private String productCode;

    @NotNull(message = "Stock is required")
    @Min(value = 2, message = "stock must be at least 2 unit")
    private int  stockAvailable;

    @NotBlank(message = "Warehouse location required")
    private  String warehouseLocation;

    @NotNull(message = "Batch number is required")
    private int batchNumber;

    @NotBlank(message = "Supplier name is required")
    private String supplier;

    @NotNull(message = "Received date is required")
    @PastOrPresent(message = "Received date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivedDate;

}
