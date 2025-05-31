package org.notifier.notificationservice.model;


import lombok.Data;

@Data
public class NotificationRequest {
    private String userId;
    private String courseId;
    private String messageType;
}
