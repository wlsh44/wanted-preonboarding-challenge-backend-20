package com.example.wantedmarket.auth.application;

import com.example.wantedmarket.auth.ui.dto.SignUpRequest;
import com.example.wantedmarket.auth.ui.dto.SignUpResponse;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        validateAlreadyExistName(request.getName());
        User user = userRepository.save(new User(request.getName()));
        return new SignUpResponse(user.getId());
    }

    private void validateAlreadyExistName(String name) {
        if (userRepository.existsByName(name)) {
            throw new WantedMarketException(ErrorCode.ALREADY_EXIST_NAME);
        }
    }
}
