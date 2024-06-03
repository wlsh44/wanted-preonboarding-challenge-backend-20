package com.example.wantedmarket.trade.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    @DisplayName("제품 판매자 id가 null이면 안 됨")
    void validateSellerId_null() throws Exception {
        assertThatThrownBy(() -> new Trade(1L, null, 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("제품 구매자 id가 null이면 안 됨")
    void validateBuyerId_null() throws Exception {
        assertThatThrownBy(() -> new Trade(null, 1L, 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("거래 제품 id가 null이면 안 됨")
    void validateProductId_null() throws Exception {
        assertThatThrownBy(() -> new Trade(1L, 1L, null))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("판매자면 true를 리턴해야 함")
    void isSellerTest() throws Exception {
        //given
        Long sellerId = 1L;
        Long buyerId = 2L;
        Trade trade = new Trade(buyerId, sellerId, 1L);

        //when
        boolean res = trade.isSeller(sellerId);

        //then
        assertThat(res).isTrue();
    }

    @Test
    @DisplayName("예약 승인 해야 함")
    void approveTest() throws Exception {
        //given
        Trade trade = new Trade(1L, 2L, 1L);

        //when


        //then

    }
}
