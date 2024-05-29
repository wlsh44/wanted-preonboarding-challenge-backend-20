package com.example.wantedmarket.product.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    public Product(Long userId, String name, int price) {
        validateUser(userId);
        validateName(name);
        validatePrice(price);
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.status = ProductStatus.ON_SALE;
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
        if (status.equals(ProductStatus.RESERVED)) {
            throw new WantedMarketException(ErrorCode.ALREADY_RESERVED_PRODUCT);
        }
        if (status.equals(ProductStatus.COMPLETED)) {
            throw new WantedMarketException(ErrorCode.ALREADY_COMPLETED_PRODUCT);
        }
        status = ProductStatus.RESERVED;
    }

    public void complete() {
        if (status.equals(ProductStatus.ON_SALE)) {
            throw new WantedMarketException(ErrorCode.NOT_RESERVED_PRODUCT);
        }
        if (status.equals(ProductStatus.COMPLETED)) {
            throw new WantedMarketException(ErrorCode.ALREADY_COMPLETED_PRODUCT);
        }
        status = ProductStatus.COMPLETED;
    }
}
