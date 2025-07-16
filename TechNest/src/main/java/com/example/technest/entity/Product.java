package com.example.technest.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.yaml.snakeyaml.events.Event;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "category_id", referencedColumnName = "id")
        private Category category;

        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Cart> cartItem;

        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<OrderItem> orderItemList;

        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<Inventory> inventoryList;


        @Column(name = "name", nullable = false, length = 100)
        private String name;

        @Column(name = "description", columnDefinition = "text")
        private String description;

        @Column(name = "unit", length = 30)
        private String unit;

        @Column(name = "price", precision = 10, scale = 2)
        private BigDecimal price;

        @Column(name = "url", length = 255)
        private String url;

        @Enumerated(EnumType.STRING)
        @Column(name = "status")
        private Status status;

        @Column(name = "created_by", length = 50)
        @CreatedBy
        private String createdBy;

        @Column(name = "created_date", nullable = false, updatable = false)
        @CreatedDate
        private Timestamp createdDate;

        @Column(name = "last_modified_by", length = 50)
        @LastModifiedBy
        private String lastModifiedBy;

        @Column(name = "last_modified_date", nullable = false)
        @LastModifiedDate
        private Timestamp lastModifiedDate;

        @Column(name = "discount", precision = 5, scale = 2)
        private BigDecimal discount;

        @Column(name = "manufacturer", length = 100)
        private String manufacturer;

        @Column(name = "warranty_period")
        private int warrantyPeriod;

        @Column(name = "product_code", length = 50)
        private String productCode;


        @Transient
        private double finalPrice;

        // Getters and Setters
        // ...
    }
