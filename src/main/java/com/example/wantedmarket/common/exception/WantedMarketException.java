package com.example.wantedmarket.common.exception;

public class WantedMarketException extends RuntimeException {

    public WantedMarketException(ErrorCode errorCode) {
        super(errorCode.getDescription());
    }
}
