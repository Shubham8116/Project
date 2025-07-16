package com.example.technest.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_number", unique = true)
    private String orderNumber;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

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

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipment_date")
    @Temporal(TemporalType.DATE)
    private Date shipmentDate;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItemList;


    @Column(name = "estimated_delivery_date")
    @Temporal(TemporalType.DATE)
    private Date estimatedDeliveryDate;

    public enum OrderStatus {
         PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
     }

    @Transient
    public String getCancelButtonLabel() {
        if (this.orderStatus == OrderStatus.CANCELLED) {
            return "Cancelled";
        } else if (this.orderStatus == OrderStatus.DELIVERED) {
            return "Delivered";
        } else {
            return "Cancel Order";
        }
    }

}
