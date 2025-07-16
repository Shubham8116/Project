package com.example.technest.dto;

import com.example.technest.markerInterface.AddMode;
import com.example.technest.markerInterface.UpdateMode;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;


@Data
public class ProductFormDto {
    @NotBlank(message = "Product name is required",groups = {AddMode.class, UpdateMode.class})
    @Size(min = 5, message = "Name must be at least 5 characters",groups = {AddMode.class, UpdateMode.class})
    private String name;

    @NotBlank(message = "Product description is required",groups = {AddMode.class, UpdateMode.class})
    @Size(min = 10, message = "Description must be at least 10 characters",groups = {AddMode.class, UpdateMode.class})
    private String description;

    @NotBlank(message = "Unit is required", groups = {AddMode.class, UpdateMode.class})
    private String unit;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive",groups = {AddMode.class, UpdateMode.class})
    private BigDecimal price;

    @NotBlank(message = "Image URL is required",groups = {AddMode.class, UpdateMode.class})
    private String url;

    @DecimalMin(value = "0.0", message = "Discount must be non-negative",groups = {AddMode.class, UpdateMode.class})
    private BigDecimal discount;

    @NotBlank(message = "Manufacturer is required",groups = {AddMode.class, UpdateMode.class})
    private String manufacturer;

    @Min(value = 2, message = "Warranty must be at least 2 month",groups = {AddMode.class, UpdateMode.class})
    private int warrantyPeriod;

    @NotBlank(message = "Product code is required",groups = {AddMode.class, UpdateMode.class})
    private String productCode;

    @NotBlank(message = "Category must be selected",groups = {AddMode.class, UpdateMode.class})
    private String categoryName;

    @Min(value = 0, message = "Stock cannot be negative",groups = AddMode.class)
    private int stockAvailable;

    @Min(value = 1, message = "Batch number must be at least 1",groups = AddMode.class)
    private int batchNumber;

    @NotBlank(message = "Warehouse location is required",groups = AddMode.class)
    private String warehouseLocation;

    @NotBlank(message = "Supplier is required",groups = AddMode.class)
    private String supplier;

    @NotNull(message = "Received date is required",groups = AddMode.class)
    @PastOrPresent(message = "Received date cannot be in the future",groups = AddMode.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receivedDate;
}
