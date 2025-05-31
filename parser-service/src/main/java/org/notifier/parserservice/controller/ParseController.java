package org.notifier.parserservice.controller;


import lombok.RequiredArgsConstructor;
import org.notifier.parserservice.exception.ParseException;
import org.notifier.parserservice.model.Course;
import org.notifier.parserservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class ParseController {

    private final CourseService courseService;

    @GetMapping("/new")
    public ResponseEntity<List<Course>> getNewCourses() {
        return ResponseEntity.ok(courseService.getNewCourses());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Course>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(category));
    }

    @PostMapping("/parse")
    public ResponseEntity<List<Course>> triggerParse() throws ParseException {
        return ResponseEntity.ok(courseService.parseAndSaveNewCourses());
    }

    @PostMapping("/mark-notified/{courseId}")
    public ResponseEntity<Void> markAsNotified(
            @PathVariable String courseId) {
        courseService.markAsNotified(courseId);
        return ResponseEntity.ok().build();
    }
}
