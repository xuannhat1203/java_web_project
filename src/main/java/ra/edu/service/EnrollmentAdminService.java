package ra.edu.service;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentAdminService {
    List<Enrollment> listEnrollmentAdmin(String search, String sortStatus, int page, int size);
    boolean changeStatus(int idEnrollment,String status);
    Long countEnrollments(String search, String statusFilter);
}
