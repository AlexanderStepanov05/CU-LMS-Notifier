package org.notifier.botservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notifier.botservice.model.User;
import org.notifier.botservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void registerUser(Long userId, String firstName, String lastName, String username) {
        if (!userRepository.existsByUserId(userId)) {
            User user = new User();
            user.setUserId(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setSubscribed(true);
            user.setLastActivity(LocalDateTime.now());

            userRepository.save(user);
            log.info("Registered new user: {}", userId);
        } else {
            updateUserActivity(userId);
        }
    }

    @Transactional
    public void subscribeUser(Long userId) {
        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setSubscribed(true);
            user.setLastActivity(LocalDateTime.now());
            userRepository.save(user);
            log.info("User subscribed: {}", userId);
        });
    }

    @Transactional
    public void unsubscribeUser(Long userId) {
        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setSubscribed(false);
            user.setLastActivity(LocalDateTime.now());
            userRepository.save(user);
            log.info("User unsubscribed: {}", userId);
        });
    }

    public List<Long> getSubscribedUsers() {
        return userRepository.findBySubscribed(true)
                .stream()
                .map(User::getUserId)
                .toList();
    }

    public void updateUserActivity(Long userId) {
        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setLastActivity(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getActiveSubscribers() {
        return userRepository.findBySubscribed(true).size();
    }
}