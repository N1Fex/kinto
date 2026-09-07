package ru.n1fex.kinto.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.n1fex.kinto.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean isUsernameAvailable(String username) {
        return userRepository.getUserByUsername(username).isEmpty();
    }

}
