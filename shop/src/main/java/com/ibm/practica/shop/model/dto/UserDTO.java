package com.ibm.practica.shop.model.dto;

import com.ibm.practica.shop.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private List<Product> productResources;
}
