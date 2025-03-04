package com.ibm.practica.shop.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User{
    @Id
    @SequenceGenerator(name = "user_sequence" , sequenceName = "user_sequence" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "user_sequence")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToMany
    private List<Product> basket;


}
