package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.repository.EnrollmentAdminRepository;
import ra.edu.entity.Enrollment;

import java.util.List;

@Service
public class EnrollmentAdminServiceImp implements EnrollmentAdminService {
    public EnrollmentAdminRepository enrollmentAdminRepository;
    public EnrollmentAdminServiceImp(EnrollmentAdminRepository enrollmentAdminRepository) {
        this.enrollmentAdminRepository = enrollmentAdminRepository;
    }
    @Override
    public List<Enrollment> listEnrollmentAdmin(String search,String sortStatus, int page, int size) {
        return enrollmentAdminRepository.listEnrollmentAdmin(search,sortStatus, page, size);
    }
    @Override
    public Long countEnrollments(String search, String statusFilter) {
        return enrollmentAdminRepository.countEnrollments(search,statusFilter);
    }
    @Override
    public boolean changeStatus(int idEnrollment,String status) {
        return enrollmentAdminRepository.changeStatus(idEnrollment,status);
    }
}
