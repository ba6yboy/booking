package com.example.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequest {

    @NotNull(message = "ID клієнта не може бути порожнім")
    private Long clientId;

    @NotNull(message = "ID послуги не може бути порожнім")
    private Long businessServiceId;

    @NotNull(message = "Час запису не може бути порожнім")
    @FutureOrPresent(message = "Неможливо створити запис у минулому")
    private LocalDateTime requestedStartTime;

    // Геттери та Сеттери
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getBusinessServiceId() { return businessServiceId; }
    public void setBusinessServiceId(Long businessServiceId) { this.businessServiceId = businessServiceId; }
    public LocalDateTime getRequestedStartTime() { return requestedStartTime; }
    public void setRequestedStartTime(LocalDateTime requestedStartTime) { this.requestedStartTime = requestedStartTime; }
}