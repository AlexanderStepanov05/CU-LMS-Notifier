package org.stepanov.telegrambotservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "org.stepanov.telegrambotservice.service")
public class TelegramBotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotServiceApplication.class, args);
    }

}
