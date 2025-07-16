package com.example.technest.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "order_item")
public class OrderItem {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;


     @Column(name = "quantity")
     private int quantity;

     @Column(name = "item_total_amount")
     private BigDecimal itemTotalAmount;

     @Column(name = "created_by")
     @CreatedBy
     private String createdBy;

     @Column(name = "created_at", updatable = false)
     @CreatedDate
     private Timestamp createdAt;

     @Column(name = "modified_by")
     @LastModifiedBy
     private String modifiedBy;

     @Column(name = "modified_at")
     @LastModifiedDate
     private Timestamp modifiedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "orders_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
