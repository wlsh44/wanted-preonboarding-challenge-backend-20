package com.example.wantedmarket.trade.ui;

import com.example.wantedmarket.common.ControllerTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.ErrorResponse;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TradeControllerTest extends ControllerTest {

    @Test
    @DisplayName("예약에 성공하면 200을 리턴해야 함")
    void reserveTest() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);

        //when then
        mockMvc.perform(post("/api/trade/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비회원이 제품을 예약하면 400을 리턴해야 함")
    void reserveTest_unauthorizedUser() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);
        ErrorResponse response = new ErrorResponse(ErrorCode.AUTHORIZE_EXCEPTION.getDescription());

        //when then
        mockMvc.perform(post("/api/trade/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 유저가 예약하면 400을 리턴해야 함")
    void reserveTest_userNotFound() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        given(tradeService.reserve(any(), any())).willThrow(new WantedMarketException(errorCode));

        //when then
        mockMvc.perform(post("/api/trade/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 제품을 예약하면 400을 리턴해야 함")
    void reserveTest_productNotFound() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);
        ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        given(tradeService.reserve(any(), any())).willThrow(new WantedMarketException(errorCode));

        //when then
        mockMvc.perform(post("/api/trade/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("판매 승인에 성공하면 200을 리턴해야 함")
    void approveSellingTest() throws Exception {
        //given

        //when then
        mockMvc.perform(post("/api/trade/approve/1")
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 유저가 판매 승인하면 400을 리턴해야 함")
    void approveSellingTest_userNotFound() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        doThrow(new WantedMarketException(errorCode)).when(tradeService).approve(any(), any());

        //when then
        mockMvc.perform(post("/api/trade/approve/1")
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("판매자가 아닌 유저가 판매 승인하면 400을 리턴해야 함")
    void approveSellingTest_notProductSeller() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.NOT_PRODUCT_SELLER;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        doThrow(new WantedMarketException(errorCode)).when(tradeService).approve(any(), any());

        //when then
        mockMvc.perform(post("/api/trade/approve/1")
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("거래가 없을 경우 400을 리턴해야 함")
    void approveSellingTest_tradeNotFound() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.TRADE_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        doThrow(new WantedMarketException(errorCode)).when(tradeService).approve(any(), any());

        //when then
        mockMvc.perform(post("/api/trade/approve/1")
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}
