package com.example.wantedmarket.product.domain;

public enum ProductStatus {
    ON_SALE("판매중"),
    RESERVED("예약중"),
    COMPLETED("완료");

    private final String ko;

    ProductStatus(String ko) {
        this.ko = ko;
    }

    public static boolean isOnSale(ProductStatus status) {
        return ON_SALE.equals(status);
    }

    public static boolean isCompleted(ProductStatus status) {
        return COMPLETED.equals(status);
    }

    public static boolean isReserved(ProductStatus status) {
        return ProductStatus.RESERVED.equals(status);
    }

    public String ko() {
        return ko;
    }
}
