package com.example.wantedmarket.auth.application;

import com.example.wantedmarket.auth.ui.dto.SignUpRequest;
import com.example.wantedmarket.auth.ui.dto.SignUpResponse;
import com.example.wantedmarket.common.annotation.ServiceTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ServiceTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 해야 함")
    void signUpTest() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("name");

        //when
        SignUpResponse response = authService.signUp(request);

        //then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원가입할 때 이미 존재하는 유저이름인 경우 예외가 발생해야 함")
    void signUpTest_alreadyExistName() throws Exception {
        //given
        userRepository.save(new User("name"));
        SignUpRequest request = new SignUpRequest("name");

        //when then
        assertThatThrownBy(() -> authService.signUp(request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.ALREADY_EXIST_NAME.getDescription());
    }
}
