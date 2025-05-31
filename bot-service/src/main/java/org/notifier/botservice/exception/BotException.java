package org.notifier.botservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BotException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BotException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
