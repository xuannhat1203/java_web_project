package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.entity.Course;
import ra.edu.repository.DashboardRepository;
import java.util.List;
@Service
public class DashboardServiceImp implements DashboardService {
    public DashboardRepository dashboardRepository;

    public DashboardServiceImp(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }
    @Override
    public Long countTotalCourses() {
        return dashboardRepository.countTotalCourses();
    }
    @Override
    public Long countTotalStudents() {
        return dashboardRepository.countTotalStudents();
    }
    @Override
    public Long countTotalEnrollments() {
        return dashboardRepository.countTotalEnrollments();
    }
    @Override
    public List<Course> getAllCourses() {
        return dashboardRepository.getAllCourses();
    }
    @Override
    public List<Course> get5bestCourses() {
        return dashboardRepository.get5bestCourses();
    }
}
