package ra.edu.repository;

import ra.edu.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository {
    List<Enrollment> listHistoryEnrollment(int id, String sortStatus, String search, int page, int size);
    Long countHistoryEnrollment(int id, String search);
    boolean cancelEnrollment(int id);
    List<Enrollment> findEnrollmentsByUserId(int idUser);
}
