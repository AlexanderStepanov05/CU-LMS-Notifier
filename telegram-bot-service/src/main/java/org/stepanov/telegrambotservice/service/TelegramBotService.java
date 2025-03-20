package org.stepanov.telegrambotservice.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stepanov.telegrambotservice.dto.TokenRequest;

@Slf4j
@Service
public class TelegramBotService {

    private final TelegramBot bot;
    private final AuthServiceClient authServiceClient;

    @Autowired
    public TelegramBotService(TelegramBot bot, AuthServiceClient authServiceClient) {
        this.bot = bot;
        this.authServiceClient = authServiceClient;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                try {
                    handleUpdate(update);
                } catch (Exception e) {
                    log.error("Error processing update: {}", update, e);
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void handleUpdate(Update update) {
        if (update == null || update.message() == null || update.message().text() == null) {
            return;
        }

        String text = update.message().text();
        Long chatId = update.message().chat().id();
        log.info("Received message: {} from chatId: {}", text, chatId);

        if (text.startsWith("/start")) {
            sendWelcomeMessage(chatId);
        } else if (text.startsWith("/auth")) {
            handleAuthCommand(chatId, text);
        } else if (text.startsWith("/help")) {
            sendHelpMessage(chatId);
        } else {
            sendMessage(chatId, "Неизвестная команда. Используйте /help");
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        String welcomeText = """
            🎉 Добро пожаловать! 🎉
            
            Я ваш бот для уведомлений из LMS
            
            Для начала работы отправьте BFF-токен
            
            Основные команды:
            /auth <BFF_TOKEN> - Авторизация
            /help - Справка
            """;
        sendMessage(chatId, welcomeText);
    }

    private void sendHelpMessage(Long chatId) {
        String helpText = """
            📚 Доступные команды:
            
            /start - Начать работу
            /auth <BFF_TOKEN> - Привязать аккаунт
            /help - Помощь
            """;
        sendMessage(chatId, helpText);
    }

    private void sendMessage(Long chatId, String text) {
        try {
            SendMessage message = new SendMessage(chatId, text);
            bot.execute(message);
            log.info("Message sent to chat {}: {}", chatId, text);
        } catch (Exception e) {
            log.error("Failed to send message to chat {}: {}", chatId, e.getMessage());
        }
    }
    private void handleAuthCommand(Long chatId, String text) {
        String[] parts = text.split(" ");
        if (parts.length != 2) {
            sendMessage(chatId, "Неверный формат команды. Пример: /auth YOUR_BFF_TOKEN");
            return;
        }

        String bffToken = parts[1];

        TokenRequest request = new TokenRequest();
        request.setChatId(chatId);
        request.setToken(bffToken);

        boolean isSuccess = authServiceClient.saveToken(request);

        if (isSuccess) {
            sendMessage(chatId, "Токен успешно сохранен! Вы будете получать уведомления.");
        } else {
            sendMessage(chatId, "Ошибка сохранения токена. Попробуйте снова.");
        }
    }

    public void sendNotification(Long chatId, String message) {
        sendMessage(chatId, "🔔 Уведомление: " + message);
    }
}
