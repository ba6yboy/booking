package com.example.booking.config;

import com.example.booking.bot.BookingTelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(BookingTelegramBot bookingTelegramBot) throws TelegramApiException {
        // Створюємо API сесію
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        // Реєструємо нашого бота
        api.registerBot(bookingTelegramBot);
        return api;
    }
}