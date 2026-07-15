package com.example.booking.service;

import com.example.booking.model.User;
import com.example.booking.model.UserRole;
import com.example.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAccountService {

    private final UserRepository userRepository;

    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User findOrCreateUser(Long chatId, String firstName) {
        return userRepository.findByTelegramChatId(chatId)
                .orElseGet(() -> {
                    User newUser = new User(
                            firstName,
                            "TG-" + chatId,
                            UserRole.CLIENT,
                            chatId
                    );
                    return userRepository.save(newUser);
                });
    }
}