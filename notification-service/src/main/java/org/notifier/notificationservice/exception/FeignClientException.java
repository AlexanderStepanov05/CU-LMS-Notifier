package org.notifier.notificationservice.exception;


public class FeignClientException extends Throwable {
    private final int status;

    public FeignClientException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
