package com.example.wantedmarket.user.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("유저 이름이 null이면 안 됨")
    void validateNameTest_null() throws Exception {
        assertThatThrownBy(() -> new User(null))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("유저 이름이 공백이면 안 됨")
    void validateNameTest_empty() throws Exception {
        assertThatThrownBy(() -> new User(""))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());

        assertThatThrownBy(() -> new User(" "))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }
}
