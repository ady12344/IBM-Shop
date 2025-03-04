package com.ibm.practica.shop.model.dto;

import com.ibm.practica.shop.model.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDTO {
    @NotNull
    private List<Product> basket;
}
