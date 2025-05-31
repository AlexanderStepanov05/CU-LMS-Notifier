package org.notifier.botservice.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final CommandHandler commandHandler;

    private TelegramBot bot;

    @PostConstruct
    public void init() {
        bot = new TelegramBot(botToken);
        setupUpdatesListener();
        log.info("Telegram Bot initialized successfully");
    }

    private void setupUpdatesListener() {
        bot.setUpdatesListener(updates -> {
            processUpdates(updates);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                log.error("Telegram API error: {} {}", e.response().errorCode(), e.response().description());
            } else {
                log.error("Telegram updates listener error", e);
            }
        });
    }

    private void processUpdates(List<Update> updates) {
        for (Update update : updates) {
            try {
                SendMessage response = commandHandler.handleUpdate(update);
                if (response != null) {
                    bot.execute(response);
                }
            } catch (Exception e) {
                log.error("Error processing update: {}", update.updateId(), e);
                if (update.message() != null) {
                    bot.execute(new SendMessage(update.message().chat().id(),
                            "⚠️ Произошла ошибка при обработке вашего запроса. Попробуйте позже."));
                }
            }
        }
    }

    public void sendMessage(Long chatId, String message) {
        try {
            bot.execute(new SendMessage(chatId, message));
            log.info("Message sent to user: {}", chatId);
        } catch (Exception e) {
            log.error("Failed to send message to user: {}", chatId, e);
        }
    }
}
