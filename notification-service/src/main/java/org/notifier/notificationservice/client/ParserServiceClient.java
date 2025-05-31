package org.notifier.notificationservice.client;


import org.notifier.notificationservice.model.Course;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(
        name = "parser-service",
        url = "${services.parser.url}"
)
public interface ParserServiceClient {

    @GetMapping("/api/courses/new")
    List<Course> getNewCourses();

    @PostMapping("/api/courses/mark-notified/{courseId}")
    void markCourseAsNotified(@PathVariable("courseId") String courseId);
}
