package org.notifier.botservice.repository;

import org.notifier.botservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserId(Long userId);
    List<User> findBySubscribed(boolean subscribed);
    boolean existsByUserId(Long userId);
}
