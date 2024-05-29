package com.example.wantedmarket.product.ui.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterProductRequest {

    @NotEmpty
    private String name;

    @PositiveOrZero
    private int price;

    @Positive
    private int quantity;
}
