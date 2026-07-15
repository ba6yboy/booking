package com.example.booking.repository;

import com.example.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring автоматично згенерує SQL: SELECT * FROM users WHERE phone = ?
    // Використовуємо Optional, бо користувача з таким телефоном може не існувати в базі
    Optional<User> findByPhone(String phone);
    Optional<User> findByTelegramChatId(Long telegramChatId);
}