package com.example.booking;

import com.example.booking.model.BusinessService;
import com.example.booking.model.User;
import com.example.booking.model.UserRole;
import com.example.booking.repository.BusinessServiceRepository;
import com.example.booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }

    // Цей код виконається автоматично один раз при кожному запуску додатка
    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, BusinessServiceRepository serviceRepository) {
        return args -> {
            // Перевіряємо, чи база порожня, щоб не дублювати дані при перезапусках
            if (userRepository.count() == 0) {
                User testClient = new User("Олексій", "+380501234567", UserRole.CLIENT);
                userRepository.save(testClient);
                System.out.println("Тестового клієнта створено з ID: " + testClient.getId());

                BusinessService testService = new BusinessService("Чоловіча стрижка", 60, new BigDecimal("500.00"));
                serviceRepository.save(testService);
                System.out.println("Тестову послугу створено з ID: " + testService.getId());
            }
        };
    }
}