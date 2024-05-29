package com.example.wantedmarket.trade.domain;

import org.springframework.context.ApplicationEvent;

public record CompleteTradeEvent(Long productId) {

}
