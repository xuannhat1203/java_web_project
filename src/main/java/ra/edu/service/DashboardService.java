package ra.edu.service;

import ra.edu.entity.Course;
import java.util.List;
public interface DashboardService {
    Long countTotalCourses();
    Long countTotalStudents();
    Long countTotalEnrollments();

    List<Course> getAllCourses();

    List<Course> get5bestCourses();
}
