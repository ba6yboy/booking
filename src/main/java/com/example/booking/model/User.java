package com.example.booking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "telegram_chat_id", unique = true)
    private Long telegramChatId; // Java-style camelCase

    // Обов'язковий конструктор для JPA
    public User() {}

    // Конструктор для звичайного створення
    public User(String name, String phone, UserRole role) {
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    // Конструктор для створення через Telegram
    public User(String name, String phone, UserRole role, Long telegramChatId) {
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.telegramChatId = telegramChatId;
    }

    // Геттери та Сеттери
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Long getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(Long telegramChatId) {
        this.telegramChatId = telegramChatId;
    }
}