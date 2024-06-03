package com.example.wantedmarket.trade.domain;

public enum TradeStatus {
    RESERVED,
    APPROVED,
    COMPLETED;

    public static boolean isReserved(TradeStatus status) {
        return RESERVED.equals(status);
    }

    public static boolean isApproved(TradeStatus status) {
        return APPROVED.equals(status);
    }
}
