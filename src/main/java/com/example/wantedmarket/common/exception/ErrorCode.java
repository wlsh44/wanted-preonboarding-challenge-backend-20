package com.example.wantedmarket.common.exception;

public enum ErrorCode {

    DOMAIN_EXCEPTION("도메인 생성 예외"),

    ALREADY_EXIST_NAME("이미 존재하는 이름"),
    AUTHORIZE_EXCEPTION("유저 인증 실패"),
    USER_NOT_FOUND("존재하지 않는 유저"),
    PRODUCT_NOT_FOUND("존재하지 않는 제품"),
    ALREADY_RESERVED_PRODUCT("이미 예약 중인 상품"),
    ALREADY_COMPLETED_PRODUCT("이미 거래 완료된 상품"),
    TRADE_NOT_FOUND("존재하지 않는 거래"),
    NOT_RESERVED_PRODUCT("예약중이지 않은 상품"),
    NOT_PRODUCT_SELLER("상품 판매자가 아님"),
    TRADABLE_PRODUCT_NOT_EXIST("거래 가능한 상품이 아님"),
    ALREADY_TRADE_PRODUCT("이미 거래 내역이 있음"),
    NOT_RESERVED_TRADE("예약된 거래가 아님");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
