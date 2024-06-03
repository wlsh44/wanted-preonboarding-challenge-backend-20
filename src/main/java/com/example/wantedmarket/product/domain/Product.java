package com.example.wantedmarket.product.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    private int price;

    private ProductStatus status;

    private int quantity;

    public Product(Long userId, String name, int price, int quantity) {
        validateUser(userId);
        validateName(name);
        validatePrice(price);
        validateQuantity(quantity);
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.status = ProductStatus.ON_SALE;
        this.quantity = quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new WantedMarketException(ErrorCode.DOMAIN_EXCEPTION);
        }
    }

    private void validateUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new WantedMarketException(ErrorCode.DOMAIN_EXCEPTION);
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new WantedMarketException(ErrorCode.DOMAIN_EXCEPTION);
        }
    }

    private void validatePrice(int price) {
        if (price < 0) {
            throw new WantedMarketException(ErrorCode.DOMAIN_EXCEPTION);
        }
    }

    public void reserve() {
        if (quantity == 0) {
            throw new WantedMarketException(ErrorCode.TRADABLE_PRODUCT_NOT_EXIST);
        }
        quantity -= 1;
        if (quantity == 0) {
            status = ProductStatus.RESERVED;
        }
    }

    public void complete() {
        if (ProductStatus.isCompleted(status)) {
            throw new WantedMarketException(ErrorCode.ALREADY_COMPLETED_PRODUCT);
        }
        status = ProductStatus.COMPLETED;
    }

    public boolean isTradable() {
        return ProductStatus.isOnSale(status);
    }

    public boolean isReserved() {
        return ProductStatus.isReserved(status);
    }
}
