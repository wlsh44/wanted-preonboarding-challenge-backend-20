package com.example.wantedmarket.user.infra;

import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class UserDummyDataInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void initData() {
        userRepository.save(new User("user1"));
        userRepository.save(new User("user2"));
        userRepository.save(new User("user3"));
        userRepository.save(new User("user4"));
    }
}
