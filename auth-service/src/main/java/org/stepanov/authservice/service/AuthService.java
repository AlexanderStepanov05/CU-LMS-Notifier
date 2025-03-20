package org.stepanov.authservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.stepanov.authservice.dto.TokenRequest;
import org.stepanov.authservice.entity.UserToken;
import org.stepanov.authservice.repository.TokenRepository;

import javax.crypto.Cipher;
import java.util.Base64;

@Service
@Transactional
public class AuthService {

    private final TokenRepository tokenRepository;
    private final Cipher cipher;

    public AuthService(TokenRepository tokenRepository, Cipher cipher) {
        this.tokenRepository = tokenRepository;
        this.cipher = cipher;
    }

    public void saveOrUpdateToken(TokenRequest request) {
        String encryptedToken = encryptToken(request.getToken());

        tokenRepository.findByChatId(request.getChatId())
                .ifPresentOrElse(
                        token -> token.setEncryptedToken(encryptedToken),
                        () -> tokenRepository.save(new UserToken(request.getChatId(), encryptedToken))
                );
    }

    private String encryptToken(String rawToken) {
        try {
            byte[] encryptedBytes = cipher.doFinal(rawToken.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
}