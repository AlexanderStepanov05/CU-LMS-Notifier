package org.notifier.parserservice.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.notifier.parserservice.model.Course;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Service
public class CourseParser {

    @Value("${parser.target.url}")
    private String targetUrl;

    @Value("${parser.timeout:5000}")
    private int timeout;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000),
            include = IOException.class
    )
    public List<Course> parseCourses() throws IOException {
        Document doc = Jsoup.connect(targetUrl)
                .timeout(timeout)
                .userAgent("Mozilla/5.0")
                .get();

        List<Course> courses = new ArrayList<>();
        Elements courseElements = doc.select(".course-card");

        for (Element element : courseElements) {
            Course course = parseCourseElement(element);
            courses.add(course);
        }

        return courses;
    }

    private Course parseCourseElement(Element element) {
        Course course = new Course();
        course.setCourseId(element.attr("data-course-id"));
        course.setTitle(element.select(".course-title").text());
        course.setDescription(element.select(".course-description").text());
        course.setUrl(element.select("a.course-link").attr("abs:href"));
        course.setCategory(element.select(".course-category").text());

        String dateStr = element.select(".course-date").text();
        course.setPublishedDate(parseDate(dateStr));

        return course;
    }

    private LocalDateTime parseDate(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
