package com.example.booking.bot;

import com.example.booking.model.User;
import com.example.booking.service.AppointmentService;
import com.example.booking.service.UserAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingTelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final AppointmentService appointmentService;
    private final UserAccountService userAccountService;

    public BookingTelegramBot(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            AppointmentService appointmentService,
            UserAccountService userAccountService) {
        super(botToken);
        this.botUsername = botUsername;
        this.appointmentService = appointmentService;
        this.userAccountService = userAccountService;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // 1. Обробка текстових повідомлень
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getFrom().getFirstName();

            if (messageText.equals("/start")) {
                User user = userAccountService.findOrCreateUser(chatId, firstName);
                sendMessage(chatId, "Привіт, " + user.getName() + "! 👋\n" +
                        "Я автоматизована система бронювання. Напиши /slots, щоб переглянути вільні години.");
            }
            else if (messageText.equals("/slots")) {
                LocalDate today = LocalDate.now();
                List<LocalTime> freeSlots = appointmentService.getAvailableTimeSlots(today);

                if (freeSlots.isEmpty()) {
                    sendMessage(chatId, "На жаль, на сьогодні вільних місць немає 😔");
                } else {
                    sendTimeSlotsKeyboard(chatId, "Вільні години на сьогодні:", freeSlots);
                }
            }
        }
        // 2. Обробка натискань на кнопки
        else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            // Надійний спосіб отримання ID користувача/чату без ризику NullPointer
            Long chatId = update.getCallbackQuery().getFrom().getId();

            if (callData.startsWith("BOOK_")) {
                String timeString = callData.substring(5);
                LocalDateTime appointmentStart = LocalDateTime.of(LocalDate.now(), LocalTime.parse(timeString));

                try {
                    User user = userAccountService.findOrCreateUser(chatId, "");
                    appointmentService.createAppointment(user.getId(), 1L, appointmentStart);

                    sendMessage(chatId, "✅ " + user.getName() + ", Ваш запис о " + timeString + " підтверджено!");
                } catch (Exception e) {
                    sendMessage(chatId, "❌ Помилка: " + e.getMessage());
                }
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTimeSlotsKeyboard(Long chatId, String text, List<LocalTime> slots) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (int i = 0; i < slots.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(slots.get(i).toString());
            button.setCallbackData("BOOK_" + slots.get(i).toString());
            row.add(button);

            if ((i + 1) % 3 == 0 || i == slots.size() - 1) {
                rows.add(row);
                row = new ArrayList<>();
            }
        }

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}


    // Оновлений конструктор
//    public BookingTelegramBot(
//            @Value("${telegram.bot.token}") String botToken,
//            @Value("${telegram.bot.username}") String botUsername,
//            AppointmentService appointmentService) { // Spring сам передасть сюди сервіс
//        super(botToken);
//        this.botUsername = botUsername;
//        this.appointmentService = appointmentService;
//    }
