package org.notifier.botservice.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notifier.botservice.client.ParserServiceClient;
import org.notifier.botservice.model.User;
import org.notifier.botservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final UserRepository userRepository;
    private final ParserServiceClient parserServiceClient;

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
            if (update.message() != null) {
                processMessage(update.message());
            }
        }
    }

    private void processMessage(Message message) {
        Long chatId = message.chat().id();
        String text = message.text();

        if (text == null) {
            sendMessage(chatId, "Пожалуйста, используйте текстовые команды");
            return;
        }

        switch (text) {
            case "/start" -> handleStart(chatId);
            case "/stop" -> handleStop(chatId);
            default -> handleToken(chatId, text);
        }
    }

    private void handleStart(Long chatId) {
        sendMessage(chatId,
                "👋 Добро пожаловать!\n\n" +
                        "Я бот для уведомлений об учебных заданиях.\n" +
                        "Отправьте ваш токен для начала работы.");
    }

    private void handleStop(Long chatId) {
        userRepository.deleteByUserId(chatId);
        sendMessage(chatId, "❌ Вы отписались от уведомлений");
    }

    private void handleToken(Long chatId, String token) {
        try {
            Object response = parserServiceClient.getTasks(token);

            User user = new User();
            user.setUserId(chatId);
            user.setToken(token);
            userRepository.save(user);

            sendMessage(chatId, "✅ Токен принят! Вы будете получать уведомления.");
        } catch (Exception e) {
            log.error("Token validation failed", e);
            sendMessage(chatId, "❌ Неверный токен. Попробуйте снова.");
        }
    }

    public void sendMessage(Long chatId, String text) {
        try {
            bot.execute(new SendMessage(chatId, text));
            log.info("Message sent to user: {}", chatId);
        } catch (Exception e) {
            log.error("Failed to send message to user: {}", chatId, e);
        }
    }
}