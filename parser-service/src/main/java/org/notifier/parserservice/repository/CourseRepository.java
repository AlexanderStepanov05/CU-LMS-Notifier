package org.notifier.parserservice.repository;

import org.notifier.parserservice.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findByCourseId(String courseId);

    @Query("{ 'notified': false }")
    List<Course> findNewCourses();

    List<Course> findByCategory(String category);
}
