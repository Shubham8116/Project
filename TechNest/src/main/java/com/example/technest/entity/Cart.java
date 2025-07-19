package com.example.technest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cart")
@EntityListeners(AuditingEntityListener.class)
public class Cart
{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "users_id", referencedColumnName = "id")
        private Users user;

        @ManyToOne
        @JsonBackReference
        @JoinColumn(name = "product_id",referencedColumnName = "id")
        private Product product;


        @Column(nullable = false)
        private int quantity;

        @Column(name = "item_total_amount", precision = 10, scale = 2, nullable = false)
        private BigDecimal itemTotalAmount;

        @Column(name = "final_price", precision = 10, scale = 2, nullable = false)
        private BigDecimal final_price;

        @Column(name = "created_by")
        @CreatedBy
        private String createdBy;

        @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        @CreatedDate
        private Timestamp createdAt;

        @Column(name = "modified_by")
        @LastModifiedBy
        private String modifiedBy;

        @Column(name = "modified_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
        @LastModifiedDate
        private Timestamp modifiedAt;



}










