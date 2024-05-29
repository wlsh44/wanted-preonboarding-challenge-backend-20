package com.example.wantedmarket.trade.application;

import com.example.wantedmarket.common.IntegrationTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.product.domain.ProductStatus;
import com.example.wantedmarket.trade.domain.CompleteTradeEvent;
import com.example.wantedmarket.trade.domain.Trade;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TradeServiceTest extends IntegrationTest {

    @Autowired
    TradeService tradeService;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("구매자가 예약하면 거래가 시작되어야 함")
    void reserveTest() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        Product product = productRepository.save(new Product(user.getId(), "name", 1000));
        ReserveRequest request = new ReserveRequest(product.getId());

        //when
        Long reserveId = tradeService.reserve(user.getId(), request);

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(reserveId).isNotNull();
        assertThat(findProduct.getStatus()).isEqualTo(ProductStatus.RESERVED);
    }

    @Test
    @DisplayName("없는 유저가 거래하는 경우 예외가 발생해야 함")
    void reserveTest_userNotFound() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);

        //when then
        assertThatThrownBy(() -> tradeService.reserve(1L, request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("없는 상품을 거래하는 경우 예외가 발생해야 함")
    void reserveTest_productNotFound() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        ReserveRequest request = new ReserveRequest(1L);

        //when then
        assertThatThrownBy(() -> tradeService.reserve(user.getId(), request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("판매자가 판매 승인을 하는 경우 판매완료 이벤트가 발생해야 함")
    void approveSellingTest() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = new Product(seller.getId(), "product", 1000);
        product.reserve();
        product = productRepository.save(product);
        Trade trade = tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId()));

        //when
        tradeService.approveSelling(seller.getId(), trade.getId());

        //then
        assertThat(events.stream(CompleteTradeEvent.class).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 중인 상품이 아닐 경우 예외가 발생해야 함")
    void approveSellingTest_notReserved() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000));
        Trade trade = tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId()));

        //when then
        assertThatThrownBy(() -> tradeService.approveSelling(seller.getId(), trade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_RESERVED_PRODUCT.getDescription());
    }

    @Test
    @DisplayName("판매자가 아닌 유저가 판매 승인을 하는 경우 예외가 발생해야 함")
    void approveSellingTest_notSeller() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000));
        Trade trade = tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId()));

        //when then
        assertThatThrownBy(() -> tradeService.approveSelling(buyer.getId(), trade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_PRODUCT_SELLER.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 유저일 경우 예외가 발생해야 함")
    void approveSellingTest_userNotFound() throws Exception {
        //given

        //when then
        assertThatThrownBy(() -> tradeService.approveSelling(1L, 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 거래일 경우 예외가 발생해야 함")
    void approveSellingTest_tradeNotFound() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000));

        //when then
        assertThatThrownBy(() -> tradeService.approveSelling(seller.getId(), 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.TRADE_NOT_FOUND.getDescription());
    }
}
