package com.example.booking.dto;

import java.time.LocalDateTime;

public class AppointmentResponse {
    private Long id;
    private String clientName;
    private String serviceTitle;
    private LocalDateTime startTime;
    private String status;

    public AppointmentResponse(Long id, String clientName, String serviceTitle, LocalDateTime startTime, String status) {
        this.id = id;
        this.clientName = clientName;
        this.serviceTitle = serviceTitle;
        this.startTime = startTime;
        this.status = status;
    }

    // Додай Геттери для всіх полів (без них Spring не зможе згенерувати JSON)
    public Long getId() { return id; }
    public String getClientName() { return clientName; }
    public String getServiceTitle() { return serviceTitle; }
    public LocalDateTime getStartTime() { return startTime; }
    public String getStatus() { return status; }
}