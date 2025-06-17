package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.entity.Enrollment;
import ra.edu.repository.EnrollmentRepository;
import java.util.List;
@Service
public class EnrollmentServiceImp implements EnrollmentService {
    public EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImp(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }
    @Override
    public List<Enrollment> listHistoryEnrollment(int id, String sortStatus, String search, int page, int size) {
        return enrollmentRepository.listHistoryEnrollment(id, sortStatus, search, page, size);
    }
    @Override
    public Long countHistoryEnrollment(int id, String search) {
        return enrollmentRepository.countHistoryEnrollment(id, search);
    }
    @Override
    public boolean cancelEnrollment(int id) {
        return enrollmentRepository.cancelEnrollment(id);
    }
    @Override
    public List<Enrollment> findEnrollmentsByUserId(int idUser) {
        return enrollmentRepository.findEnrollmentsByUserId(idUser);
    }
}
