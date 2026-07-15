package com.example.booking.service;

import com.example.booking.model.Appointment;
import com.example.booking.model.AppointmentStatus;
import com.example.booking.model.BusinessService;
import com.example.booking.model.User;
import com.example.booking.repository.AppointmentRepository;
import com.example.booking.repository.BusinessServiceRepository;
import com.example.booking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final BusinessServiceRepository businessServiceRepository;

    // Використовуємо Constructor Injection — це best practice у Spring
    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserRepository userRepository,
                              BusinessServiceRepository businessServiceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.businessServiceRepository = businessServiceRepository;
    }

    // Метод для отримання всіх записів (наприклад, для панелі адміністратора)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Метод для отримання розкладу на конкретний день
    public List<Appointment> getAppointmentsForDate(java.time.LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return appointmentRepository.findByStartTimeBetween(startOfDay, endOfDay);
    }

    @Transactional
    public Appointment createAppointment(Long clientId, Long serviceId, LocalDateTime requestedStartTime) {
        // 1. Отримуємо клієнта та послугу з БД, або кидаємо виняток, якщо їх немає
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Клієнта з таким ID не знайдено"));

        BusinessService requestedBusinessService = businessServiceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Послугу з таким ID не знайдено"));

        // 2. Обчислюємо час завершення нової послуги
        LocalDateTime requestedEndTime = requestedStartTime.plusMinutes(requestedBusinessService.getDurationMinutes());

        // 3. Перевірка доступності часу
        checkTimeAvailability(requestedStartTime, requestedEndTime);

        // 4. Якщо все добре — створюємо запис
        Appointment appointment = new Appointment(client, requestedBusinessService, requestedStartTime, AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    private void checkTimeAvailability(LocalDateTime newStartTime, LocalDateTime newEndTime) {
        // Отримуємо початок і кінець дня, на який клієнт хоче записатися
        LocalDateTime startOfDay = newStartTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        // Витягуємо всі існуючі записи на цей день
        List<Appointment> dailyAppointments = appointmentRepository.findByStartTimeBetween(startOfDay, endOfDay);

        // Перевіряємо, чи є перетин часу (Time Overlap)
        boolean isTimeTaken = dailyAppointments.stream().anyMatch(existingAppointment -> {
            LocalDateTime existingStartTime = existingAppointment.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(
                    existingAppointment.getService().getDurationMinutes()
            );

            // Логіка перетину двох часових відрізків:
            // (Початок нового < Кінець існуючого) І (Кінець нового > Початок існуючого)
            return newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime);
        });

        if (isTimeTaken) {
            // У реальному проекті тут краще створити свій кастомний Exception (наприклад, TimeSlotTakenException)
            throw new IllegalStateException("Обраний час вже зайнятий або перетинається з іншим записом.");
        }
    }
    public List<LocalTime> getAvailableTimeSlots(LocalDate date) {
        // 1. Створюємо список усіх можливих годин (з 10:00 до 17:00)
        List<LocalTime> allSlots = new ArrayList<>();
        for (int i = 10; i < 18; i++) {
            allSlots.add(LocalTime.of(i, 0));
        }

        // 2. Отримуємо всі зайняті записи на цей день (використовуємо наш старий метод)
        List<Appointment> bookedAppointments = getAppointmentsForDate(date);

        // 3. Витягуємо з них тільки години початку
        List<LocalTime> bookedTimes = bookedAppointments.stream()
                .map(app -> app.getStartTime().toLocalTime())
                .toList();

        // 4. Видаляємо зайняті години зі списку всіх годин
        allSlots.removeAll(bookedTimes);

        return allSlots; // Повертаємо тільки те, що лишилося вільним
    }
}