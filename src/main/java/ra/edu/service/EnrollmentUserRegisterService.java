package ra.edu.service;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentUserRegisterService {
    public List<Enrollment> findAllRegister();
    public boolean checkEnrollment(int userId, int courseId);
    public Enrollment findById(int id);
    public boolean registerCourse(Enrollment enrollment);
}
