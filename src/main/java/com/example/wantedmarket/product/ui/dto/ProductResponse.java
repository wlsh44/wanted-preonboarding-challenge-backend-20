package com.example.wantedmarket.product.ui.dto;

import com.example.wantedmarket.product.domain.Product;
import lombok.Data;

@Data
public class ProductResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final String status;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.status = product.getStatus().ko();
    }
}
