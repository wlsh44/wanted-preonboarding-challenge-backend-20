package com.example.wantedmarket.auth.ui;

import com.example.wantedmarket.auth.ui.dto.SignUpRequest;
import com.example.wantedmarket.auth.ui.dto.SignUpResponse;
import com.example.wantedmarket.common.ControllerTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.ErrorResponse;
import com.example.wantedmarket.common.exception.WantedMarketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원가입에 성공하면 200과 유저 id를 리턴해야 함")
    void signUpTest() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("name");
        SignUpResponse expect = new SignUpResponse(1L);
        given(authService.signUp(any())).willReturn(expect);

        //when then
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }

    @Test
    @DisplayName("회원가입에 실패하면 400을 리턴해야 함")
    void signUpTest_fail() throws Exception {
        //given
        ErrorCode error = ErrorCode.ALREADY_EXIST_NAME;
        ErrorResponse expect = new ErrorResponse(error.getDescription());
        given(authService.signUp(any())).willThrow(new WantedMarketException(error));

        //when then
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new SignUpRequest("name"))))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(expect)));
    }
}
