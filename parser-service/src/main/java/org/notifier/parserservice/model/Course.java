package org.notifier.parserservice.model;

import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "courses")
public class Course {
    @Id
    private String id;
    private String courseId; // Уникальный ID с сайта
    private String title;
    private String description;
    private String url;
    private String category;
    private LocalDateTime publishedDate;
    private boolean notified = false;
}