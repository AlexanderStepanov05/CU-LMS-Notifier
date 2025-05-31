package org.notifier.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notifier.notificationservice.client.BotServiceClient;
import org.notifier.notificationservice.client.ParserServiceClient;
import org.notifier.notificationservice.model.Course;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ParserServiceClient parserServiceClient;
    private final BotServiceClient botServiceClient;

    // Запускается каждые 5 минут (300000 мс)
    @Scheduled(fixedRate = 300000)
    public void checkAndSendNotifications() {
        log.info("Starting notification check...");

        try {
            // 1. Получаем новые курсы из сервиса парсинга
            List<Course> newCourses = parserServiceClient.getNewCourses();

            if (newCourses.isEmpty()) {
                log.info("No new courses found");
                return;
            }

            log.info("Found {} new courses", newCourses.size());

            // 2. Обрабатываем каждый новый курс
            for (Course course : newCourses) {
                processCourse(course);
            }

        } catch (Exception e) {
            log.error("Error during notification processing", e);
        }
    }

    private void processCourse(Course course) {
        try {
            log.info("Processing course: {}", course.getTitle());

            // 3. Формируем сообщение для пользователей
            String message = formatCourseMessage(course);

            // 4. Получаем подписанных пользователей
            List<Long> subscribers = getSubscribers();

            // 5. Отправляем уведомления
            for (Long userId : subscribers) {
                sendNotification(userId, message);
            }

            // 6. Помечаем курс как обработанный
            parserServiceClient.markCourseAsNotified(course.getCourseId());
            log.info("Course {} marked as notified", course.getCourseId());

        } catch (Exception e) {
            log.error("Failed to process course: {}", course.getCourseId(), e);
        }
    }

    private String formatCourseMessage(Course course) {
        return "🎓 Новый курс! 🎓\n" +
                "**" + course.getTitle() + "**\n" +
                course.getDescription() + "\n\n" +
                "Ссылка: " + course.getUrl() + "\n" +
                "Категория: #" + course.getCategory();
    }

    private List<Long> getSubscribers() {
        // В реальной системе здесь бы был запрос к сервису пользователей
        // Для упрощения возвращаем статический список
        return List.of(12345L, 67890L);
    }

    private void sendNotification(Long userId, String message) {
        try {
            // 7. Отправляем сообщение через сервис бота
            botServiceClient.sendNotification(userId, message);
            log.info("Notification sent to user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to send notification to user: {}", userId, e);
        }
    }
}