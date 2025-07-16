package com.example.technest.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name")
    private String roleName;


    @ManyToMany(mappedBy = "roles")
    private Set<Users> users=new HashSet<>();




}
