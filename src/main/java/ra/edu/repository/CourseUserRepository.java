package ra.edu.repository;

import ra.edu.entity.Course;

import java.util.List;

public interface CourseUserRepository {
    List<Course> searchCoursesByName(String name);
    List<Course> findAllCourses();
}
