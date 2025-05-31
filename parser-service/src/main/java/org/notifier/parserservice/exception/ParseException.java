package org.notifier.parserservice.exception;

public class ParseException extends Throwable {
    public ParseException(String message, Exception cause) {
        super(message, cause);
    }
}
