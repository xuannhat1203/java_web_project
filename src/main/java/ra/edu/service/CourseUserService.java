package ra.edu.service;

import ra.edu.entity.Course;

import java.util.List;

public interface CourseUserService {
    List<Course> searchCoursesByName(String name);
    List<Course> findAllCourses();
}
