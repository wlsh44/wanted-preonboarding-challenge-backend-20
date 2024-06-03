package com.example.wantedmarket.trade.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.TradeCompletedEvent;
import com.example.wantedmarket.trade.domain.TradeReservedEvent;
import com.example.wantedmarket.trade.domain.Trade;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import com.example.wantedmarket.trade.ui.dto.TradeResponse;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TradeRepository tradeRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long reserve(Long buyerId, ReserveRequest request) {
        validateUser(buyerId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        validateAlreadyTrade(buyerId, product.getId());
        validateTradableProduct(product);
        Trade trade = tradeRepository.save(new Trade(buyerId, product.getUserId(), request.getProductId(), product.getPrice()));
        publisher.publishEvent(new TradeReservedEvent(product.getId()));
        return trade.getId();
    }

    private void validateAlreadyTrade(Long buyerId, Long productId) {
        if (tradeRepository.existsByBuyerIdAndProductId(buyerId, productId)) {
            throw new WantedMarketException(ErrorCode.ALREADY_TRADE_PRODUCT);
        }
    }

    private void validateTradableProduct(Product product) {
        if (!product.isTradable()) {
            throw new WantedMarketException(ErrorCode.TRADABLE_PRODUCT_NOT_EXIST);
        }
    }

    private void validateUser(Long buyerId) {
        if (!userRepository.existsById(buyerId)) {
            throw new WantedMarketException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public void approve(Long sellerId, Long tradeId) {
        validateUser(sellerId);

        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.TRADE_NOT_FOUND));

        validateSeller(sellerId, trade);
        trade.approve();
    }

    private void validateSeller(Long sellerId, Trade trade) {
        if (!trade.isSeller(sellerId)) {
            throw new WantedMarketException(ErrorCode.NOT_PRODUCT_SELLER);
        }
    }

    @Transactional
    public void complete(Long buyerId, Long tradeId) {
        validateUser(buyerId);

        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.TRADE_NOT_FOUND));

        validateBuyer(buyerId, trade);
        trade.complete();
        publisher.publishEvent(new TradeCompletedEvent(trade.getProductId()));
    }

    private void validateBuyer(Long buyerId, Trade trade) {
        if (!trade.isBuyer(buyerId)) {
            throw new WantedMarketException(ErrorCode.NOT_PRODUCT_BUYER);
        }
    }

    public TradeResponse findTrade(Long userId, Long productId) {
        return tradeRepository.findByProductIdWithTraderId(productId, userId)
                .map(trade -> {
                    User buyer = userRepository.findById(trade.getBuyerId())
                            .orElseThrow(() -> new WantedMarketException(ErrorCode.USER_NOT_FOUND));
                    User seller = userRepository.findById(trade.getSellerId())
                            .orElseThrow(() -> new WantedMarketException(ErrorCode.USER_NOT_FOUND));
                    return new TradeResponse(trade.getId(), buyer, seller, trade.getTradeInfo().getPrice());
                })
                .orElseGet(TradeResponse::new);
    }
}
