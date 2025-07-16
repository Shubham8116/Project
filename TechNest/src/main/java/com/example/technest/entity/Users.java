package com.example.technest.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{1,30}$", message = "Name must contain only letters and spaces, and be between 1 and 30 characters long")
    @Column(name ="name")
    private String name;

    @NotEmpty(message = "email is required")
    @Email(message = "Please enter a valid email Id")
    //@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address")
    @Column(name ="email")
    private String email;

    @NotEmpty(message = "password is required")
    //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must be at least 8 characters long and contain at least one letter and one number")
    @Size(min = 8, max = 60, message = "password must be between 8 and 128 characters")
    @Column(name ="password")
    private String password;


    @Column(name ="enabled")
    private Boolean enabled=true;

    @Valid
    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private UsersDetails usersDetails;



    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }) // if we write cascadeType.All then role from role table can also be deleted to avoid it we have not used All.
    @JoinTable(
            name = "users_role",//new table will be created
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cart> cartItem;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders;

    // Getters and setters
}

