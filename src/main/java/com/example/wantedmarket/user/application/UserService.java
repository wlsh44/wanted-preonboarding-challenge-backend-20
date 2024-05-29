package com.example.wantedmarket.user.application;

import com.example.wantedmarket.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean existById(Long userId) {
        return userRepository.existsById(userId);
    }
}
