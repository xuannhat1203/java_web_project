package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.entity.Enrollment;
import ra.edu.repository.EnrollmentUserRegisterRepository;

import java.util.List;

@Service
public class EnrollmentUserRegisterServiceImp implements EnrollmentUserRegisterService {
    public EnrollmentUserRegisterRepository enrollmentUserRegisterRepository;;
    public EnrollmentUserRegisterServiceImp(EnrollmentUserRegisterRepository enrollmentUserRegisterRepository) {
        this.enrollmentUserRegisterRepository = enrollmentUserRegisterRepository;
    }
    @Override
    public boolean registerCourse(Enrollment enrollment) {
        return enrollmentUserRegisterRepository.registerCourse(enrollment);
    }
    @Override
    public Enrollment findById(int id) {
        return enrollmentUserRegisterRepository.findById(id);
    }
    @Override
    public boolean checkEnrollment(int userId, int courseId) {
        return enrollmentUserRegisterRepository.checkEnrollment(userId,courseId);
    }
    @Override
    public List<Enrollment> findAllRegister() {
        return enrollmentUserRegisterRepository.findAllRegister();
    }

}
