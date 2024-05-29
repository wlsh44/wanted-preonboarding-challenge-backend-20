package com.example.wantedmarket.product.domain;

public enum ProductStatus {
    ON_SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("완료");

    private final String ko;

    ProductStatus(String ko) {
        this.ko = ko;
    }

    public String ko() {
        return ko;
    }
}
