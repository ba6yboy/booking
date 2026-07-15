package com.example.booking.controller;

import com.example.booking.dto.AppointmentRequest;
import com.example.booking.dto.AppointmentResponse;
import com.example.booking.model.Appointment;
import com.example.booking.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Бронювання", description = "Контролер для управління записами клієнтів") // Опис блоку
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(
            summary = "Створити новий запис",
            description = "Перевіряє доступність часу і створює бронювання послуги для клієнта"
    ) // Опис конкретного методу
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        try {
            Appointment newAppointment = appointmentService.createAppointment(
                    request.getClientId(),
                    request.getBusinessServiceId(),
                    request.getRequestedStartTime()
            );

            // Створюємо URL до нового ресурсу (наприклад: http://localhost:8080/api/appointments/5)
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newAppointment.getId())
                    .toUri();

            // Повертаємо 201 Created із заголовком Location, але БЕЗ тіла (body)
            return ResponseEntity.created(location).build();

        } catch (IllegalStateException | IllegalArgumentException e) {
            // Замість e.getMessage() повертаємо статичний текст.
            // Це гарантує відсутність витоку даних або XSS.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Неможливо створити запис. Перевірте введені дані або доступність часу."));
        }
    }

    @GetMapping
    @Operation(summary = "Отримати всі записи", description = "Повертає повну історію всіх бронювань у системі")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();

        // Перетворюємо список Appointment на список AppointmentResponse
        List<AppointmentResponse> responseList = appointments.stream()
                .map(app -> new AppointmentResponse(
                        app.getId(),
                        app.getClient().getName(),
                        app.getService().getTitle(),
                        app.getStartTime(),
                        app.getStatus().name()
                ))
                .collect(Collectors.toList()); // Якщо у тебе Java 16+, можна просто написати .toList()

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/daily")
    @Operation(summary = "Розклад на день", description = "Повертає всі записи на обрану дату (формат YYYY-MM-DD)")
    public ResponseEntity<List<AppointmentResponse>> getDailySchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Appointment> appointments = appointmentService.getAppointmentsForDate(date);

        List<AppointmentResponse> responseList = appointments.stream()
                .map(app -> new AppointmentResponse(
                        app.getId(),
                        app.getClient().getName(),
                        app.getService().getTitle(),
                        app.getStartTime(),
                        app.getStatus().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

}