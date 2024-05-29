package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.CompleteTradeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaleCompletedEventListener {

    private final ProductRepository productRepository;

    @EventListener
    @Transactional
    public void completeProduct(CompleteTradeEvent event) {
        Long productId = event.productId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        product.complete();
    }
}
