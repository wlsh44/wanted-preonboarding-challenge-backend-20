package com.example.wantedmarket.product.ui;

import com.example.wantedmarket.common.ControllerTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.ErrorResponse;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.ui.dto.ProductResponse;
import com.example.wantedmarket.product.ui.dto.RegisterProductRequest;
import com.example.wantedmarket.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTest {

    @Test
    @DisplayName("제품을 등록하면 200을 리턴해야 함")
    void registerProductTest() throws Exception {
        //given
        RegisterProductRequest request = new RegisterProductRequest("name", 1000);

        //when then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비회원이 제품을 등록하면 400을 리턴해야 함")
    void registerProductTest_unauthorizedUser() throws Exception {
        //given
        RegisterProductRequest request = new RegisterProductRequest("name", 1000);
        ErrorResponse response = new ErrorResponse(ErrorCode.AUTHORIZE_EXCEPTION.getDescription());

        //when then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 유저가 제품을 등록하면 400을 리턴해야 함")
    void registerProductTest_userNotFound() throws Exception {
        //given
        RegisterProductRequest request = new RegisterProductRequest("name", 1000);
        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        given(productService.registerProduct(any(), any()))
                .willThrow(new WantedMarketException(errorCode));

        //when then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("제품을 상세조회하면 200과 상세 내용을 리턴해야 함")
    void findProductTest_unauthorizedUser() throws Exception {
        //given
        ProductResponse response = new ProductResponse(new Product(1L, "name", 1000));
        given(productService.findProduct(any()))
                .willReturn(response);

        //when then
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("없는 유저가 제품을 등록하면 400을 리턴해야 함")
    void findProductTest_productNotFound() throws Exception {
        //given
        ErrorCode errorCode = ErrorCode.PRODUCT_NOT_FOUND;
        ErrorResponse response = new ErrorResponse(errorCode.getDescription());
        given(productService.findProduct(any()))
                .willThrow(new WantedMarketException(errorCode));

        //when then
        mockMvc.perform(get("/api/product/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }
}
