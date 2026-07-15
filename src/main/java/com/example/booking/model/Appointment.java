package com.example.booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Багато записів можуть належати одному клієнту
    @JoinColumn(name = "user_id", nullable = false) // Назва зовнішнього ключа (Foreign Key) в БД
    private User client;

    @ManyToOne // Багато записів можуть стосуватися однієї послуги
    @JoinColumn(name = "service_id", nullable = false)
    private BusinessService businessService;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // Дата та час початку сеансу

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    public Appointment() {}

    public Appointment(User client, BusinessService businessService, LocalDateTime startTime, AppointmentStatus status) {
        this.client = client;
        this.businessService = businessService;
        this.startTime = startTime;
        this.status = status;
    }

    // Геттери та Сеттери
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getClient() { return client; }
    public void setClient(User client) { this.client = client; }
    public BusinessService getService() { return businessService; }
    public void setService(BusinessService businessService) { this.businessService = businessService; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}