package com.example.wantedmarket.trade.domain;

import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TradeRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Test
    @DisplayName("거래 중인 상품이 있을 경우 true를 반환해야 함")
    void existsByOnTradingTest_true() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "name", 1000, 1));
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId(), 1000);
        tradeRepository.save(trade);

        //when
        boolean res = tradeRepository.existsByOnTrading(product.getId());

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("거래 가능한 상품이 없을 경우 false를 반환해야 함")
    void existsByOnTradingTest_false() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "name", 1000, 0));
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId(), 1000);
        trade.approve();
        trade.complete();
        tradeRepository.save(trade);

        //when
        boolean res = tradeRepository.existsByOnTrading(product.getId());

        //then
        assertThat(res).isFalse();
    }
}
