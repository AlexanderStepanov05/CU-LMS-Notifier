package org.stepanov.telegrambotservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    private Long chatId;
    private String token;
}