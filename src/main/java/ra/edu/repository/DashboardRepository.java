package ra.edu.repository;

import ra.edu.entity.Course;
import java.util.List;

public interface DashboardRepository {
    Long countTotalCourses();
    Long countTotalStudents();
    Long countTotalEnrollments();
    List<Course> getAllCourses();
    List<Course> get5bestCourses();
}
