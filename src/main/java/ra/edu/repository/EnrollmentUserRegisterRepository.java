package ra.edu.repository;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentUserRegisterRepository {
    public List<Enrollment> findAllRegister();
    public boolean checkEnrollment(int userId, int courseId);
    public Enrollment findById(int id);
    public boolean registerCourse(Enrollment enrollment);
}
