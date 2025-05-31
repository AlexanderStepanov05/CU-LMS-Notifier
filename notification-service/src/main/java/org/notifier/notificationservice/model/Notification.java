package org.notifier.notificationservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private Long userId;
    private String courseId;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime sentAt;
    private int attemptCount;
    private String errorMessage;
}
