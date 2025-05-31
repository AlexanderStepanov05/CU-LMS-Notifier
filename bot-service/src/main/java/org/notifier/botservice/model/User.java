package org.notifier.botservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private boolean subscribed = true;
    private LocalDateTime registrationDate = LocalDateTime.now();
    private LocalDateTime lastActivity;
}
