package com.example.technest.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UsersDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotNull(message = "Phone number is required")
    @Size(min = 10, max = 10, message = "Enter valid phone number")
    @Column(name ="phone")
    private String phone;

    @NotNull(message = "Date of birth is required")
    @PastOrPresent(message = "Birthdate date cannot be in the future")
    @Column(name ="dob")
    private LocalDate dob;

    @NotNull(message = "Street is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.]{1,30}$", message = "Street must be between 1 to 30 characters long and may include letters, numbers, spaces, commas, or periods.")    @Column(name ="street")
    private String street;

    @NotNull(message = "City is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{1,20}$", message = "City must contain only letters and spaces, and be between 1 and 20 characters long")
    @Column(name ="city")
    private String city;

    @NotNull(message = "State is required")
    @Column(name ="state")
    private String state;

    @NotNull(message = "Pin code is required")
    @Pattern(regexp = "^\\d{6}$", message = "Pin code must be 6 digits")
    @Column(name ="pin_code")
    private String pinCode;

    @NotNull(message = "Preferred payment method is required")
    @Column(name ="payment_method")
    private String paymentMethod;

    @CreatedDate
    @Column(name ="created_at")
    private LocalDate createdAt;

    @LastModifiedDate
    @Column(name ="modified_at")
    private LocalDate modifiedAt;

    @CreatedBy
    @Column(name ="created_by")
    private String createdBy;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id")
    private Users users;



//
//    @PrePersist
//    public void prePersist() {
//        this.createdAt = LocalDate.now();
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        this.modifiedAt = LocalDate.now();

    }


