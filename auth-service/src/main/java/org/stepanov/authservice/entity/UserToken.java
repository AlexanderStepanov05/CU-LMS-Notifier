package org.stepanov.authservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tokens")
public class UserToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String encryptedToken;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public UserToken(Long chatId, String encryptedToken) {
        this.chatId = chatId;
        this.encryptedToken = encryptedToken;
    }
}
