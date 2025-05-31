package org.notifier.notificationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(name = "bot-service", url = "${services.bot.url}")
public interface BotServiceClient {

    @PostMapping("/bot/send-notification")
    void sendNotification(
            @RequestParam("userId") Long userId,
            @RequestParam("message") String message
    );
}
