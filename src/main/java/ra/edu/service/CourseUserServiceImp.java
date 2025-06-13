package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.entity.Course;
import ra.edu.repository.CourseUserRepository;
import java.util.List;
@Service
public class CourseUserServiceImp implements CourseUserService {
    public CourseUserRepository courseUserRepository;

    public CourseUserServiceImp(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }
    @Override
    public List<Course> searchCoursesByName(String name) {
        return courseUserRepository.searchCoursesByName(name);
    }

    @Override
    public List<Course> findAllCourses() {
        return courseUserRepository.findAllCourses();
    }
}
