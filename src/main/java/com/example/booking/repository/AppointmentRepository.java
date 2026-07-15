package com.example.booking.repository;

import com.example.booking.model.Appointment;
import com.example.booking.model.AppointmentStatus;
import com.example.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. Отримати історію записів конкретного клієнта
    List<Appointment> findByClient(User client);

    // 2. Отримати всі записи за певний статус (наприклад, очікують підтвердження адміном)
    List<Appointment> findByStatus(AppointmentStatus status);

    // 3. Знайти всі записи у проміжку часу (дуже важливо для перевірки зайнятих слотів на день чи тиждень)
    List<Appointment> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}