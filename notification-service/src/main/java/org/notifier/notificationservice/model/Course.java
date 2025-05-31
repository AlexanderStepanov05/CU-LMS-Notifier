package org.notifier.notificationservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Course {
    private String courseId;
    private String title;
    private String description;
    private String url;
    private String category;
    private LocalDateTime publishedDate;
}
