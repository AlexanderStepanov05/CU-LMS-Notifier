package org.notifier.parserservice.service;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notifier.parserservice.exception.ParseException;
import org.notifier.parserservice.model.Course;
import org.notifier.parserservice.repository.CourseRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseParser courseParser;

    @Value("${lms.base-url}")
    private String lmsBaseUrl;

    private final WebClient webClient = WebClient.create();

    @CacheEvict(value = "courses", allEntries = true)
    public List<Course> parseAndSaveNewCourses() throws ParseException {
        try {
            List<Course> parsedCourses = courseParser.parseCourses();
            return saveNewCourses(parsedCourses);
        } catch (Exception e) {
            log.error("Error parsing courses", e);
            throw new ParseException("Failed to parse courses", e);
        }
    }

    @Retryable(maxAttempts = 3, include = DuplicateKeyException.class)
    private List<Course> saveNewCourses(List<Course> courses) {
        List<Course> newCourses = new ArrayList<>();

        for (Course course : courses) {
            courseRepository.findByCourseId(course.getCourseId())
                    .ifPresentOrElse(
                            existing -> log.debug("Course already exists: {}", existing.getCourseId()),
                            () -> {
                                course.setNotified(false);
                                Course saved = courseRepository.save(course);
                                newCourses.add(saved);
                                log.info("Saved new course: {}", saved.getTitle());
                            }
                    );
        }

        return newCourses;
    }

    @Cacheable(value = "courses", key = "#category", unless = "#result.isEmpty()")
    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    @Cacheable(value = "newCourses")
    public List<Course> getNewCourses() {
        return courseRepository.findNewCourses();
    }

    public void markAsNotified(String courseId) {
        courseRepository.findByCourseId(courseId).ifPresent(course -> {
            course.setNotified(true);
            courseRepository.save(course);
        });
    }

    public boolean checkToken(String token) {
        return webClient.get()
                .uri(lmsBaseUrl + "/account/me")
                .cookie("bff.cookie", token)
                .retrieve()
                .onStatus(HttpStatus::isError, resp -> Mono.empty())
                .toBodilessEntity()
                .map(resp -> resp.getStatusCode().is2xxSuccessful())
                .blockOptional()
                .orElse(false);
    }

    public List<Object> getAllTasks(String token) {
        return webClient.get()
                .uri(lmsBaseUrl + "/micro-lms/tasks/student")
                .cookie("bff.cookie", token)
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();
    }
}
