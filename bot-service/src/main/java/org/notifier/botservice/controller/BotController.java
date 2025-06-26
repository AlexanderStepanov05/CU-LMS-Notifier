package org.notifier.botservice.controller;

import lombok.RequiredArgsConstructor;
import org.notifier.botservice.service.TelegramBotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotController {

    private final TelegramBotService telegramBotService;

    @PostMapping("/send-notification")
    public void sendNotification(
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message) {
        telegramBotService.sendMessage(userId, message);
    }
}
