package org.notifier.botservice.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final UserService userService;

    public SendMessage handleUpdate(Update update) {
        if (update.message() == null) {
            return null;
        }

        Long chatId = update.message().chat().id();
        String text = update.message().text();
        String firstName = update.message().chat().firstName();
        String lastName = update.message().chat().lastName();
        String username = update.message().chat().username();

        // Регистрируем/обновляем пользователя
        userService.registerUser(chatId, firstName, lastName, username);

        if (text == null) {
            return new SendMessage(chatId, "Пожалуйста, используйте текстовые команды");
        }

        switch (text) {
            case "/start":
                return handleStart(chatId);
            case "/subscribe":
                return handleSubscribe(chatId);
            case "/unsubscribe":
                return handleUnsubscribe(chatId);
            case "/help":
                return handleHelp(chatId);
            default:
                return handleUnknownCommand(chatId);
        }
    }

    private SendMessage handleStart(Long chatId) {
        String welcomeMessage = "👋 Добро пожаловать!\n\n" +
                "Я бот для уведомлений о новых курсах.\n" +
                "Используйте /subscribe чтобы подписаться на обновления\n" +
                "/unsubscribe чтобы отписаться\n" +
                "/help для помощи";

        return new SendMessage(chatId, welcomeMessage);
    }

    private SendMessage handleSubscribe(Long chatId) {
        userService.subscribeUser(chatId);
        return new SendMessage(chatId, "✅ Вы успешно подписались на обновления!");
    }

    private SendMessage handleUnsubscribe(Long chatId) {
        userService.unsubscribeUser(chatId);
        return new SendMessage(chatId, "❌ Вы отписались от обновлений. " +
                "Используйте /subscribe чтобы подписаться снова.");
    }

    private SendMessage handleHelp(Long chatId) {
        String helpMessage = "📖 Доступные команды:\n\n" +
                "/start - Начать работу с ботом\n" +
                "/subscribe - Подписаться на обновления курсов\n" +
                "/unsubscribe - Отписаться от обновлений\n" +
                "/help - Показать эту справку";

        return new SendMessage(chatId, helpMessage);
    }

    private SendMessage handleUnknownCommand(Long chatId) {
        return new SendMessage(chatId, "⚠️ Неизвестная команда. Используйте /help для списка команд");
    }
}
