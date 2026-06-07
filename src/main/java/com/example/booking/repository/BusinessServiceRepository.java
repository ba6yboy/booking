package com.example.booking.repository;

import com.example.booking.model.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<UserService, Long> {
    // Тут залишаємо інтерфейс порожнім. Усі CRUD-методи вже успадковані від JpaRepository.
}