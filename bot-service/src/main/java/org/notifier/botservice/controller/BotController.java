package org.notifier.botservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.notifier.botservice.service.TelegramBotService;
import org.notifier.botservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bot")
@RequiredArgsConstructor
@Tag(name = "Bot API", description = "Управление Telegram ботом")
public class BotController {

    private final TelegramBotService telegramBotService;
    private final UserService userService;

    @PostMapping("/send-notification")
    @Operation(summary = "Отправить уведомление пользователю")
    public ResponseEntity<Void> sendNotification(
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message) {
        telegramBotService.sendMessage(userId, message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/broadcast")
    @Operation(summary = "Рассылка сообщения всем подписчикам")
    public ResponseEntity<Void> broadcastMessage(
            @RequestParam("message") String message) {
        List<Long> subscribers = userService.getSubscribedUsers();
        subscribers.forEach(userId -> telegramBotService.sendMessage(userId, message));
        return ResponseEntity.ok().build();
    }
}
