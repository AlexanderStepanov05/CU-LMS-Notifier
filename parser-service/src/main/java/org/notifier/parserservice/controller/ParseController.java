package org.notifier.parserservice.controller;


import lombok.RequiredArgsConstructor;
import org.notifier.parserservice.exception.ParseException;
import org.notifier.parserservice.model.Course;
import org.notifier.parserservice.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

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

    @GetMapping("/api/tasks")
    public ResponseEntity<?> getUserTasks(HttpServletRequest request) {
        String bffToken = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("BFF".equals(cookie.getName()) || "bff.cookie".equals(cookie.getName())) {
                    bffToken = cookie.getValue();
                    break;
                }
            }
        }
        if (bffToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No BFF token");
        }
        if (!courseService.checkToken(bffToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        var tasks = courseService.getAllTasks(bffToken);
        return ResponseEntity.ok(tasks);
    }
}
