package org.notifier.notificationservice.repository;

import org.notifier.notificationservice.model.Notification;
import org.notifier.notificationservice.model.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findByStatusAndAttemptCountLessThan(
            NotificationStatus status,
            int maxAttempts,
            Pageable pageable
    );

    Optional<Notification> findByUserIdAndCourseId(Long userId, String courseId);
}
