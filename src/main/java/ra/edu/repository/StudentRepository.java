package ra.edu.repository;

import ra.edu.entity.User;

import java.util.List;

public interface StudentRepository {
    List<User> listStudentPagination(String search, String sort, int page, int size);
    boolean updateStatus(int id);
    long countWithFilter(String keyword);
}
