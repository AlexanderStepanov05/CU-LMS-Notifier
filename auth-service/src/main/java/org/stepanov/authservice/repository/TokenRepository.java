package org.stepanov.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stepanov.authservice.entity.UserToken;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByChatId(Long chatId);

    boolean existsByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
