package com.example.booking.repository;

import com.example.booking.model.BusinessService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, Long> {
    // Тут залишаємо інтерфейс порожнім. Усі CRUD-методи вже успадковані від JpaRepository.
}