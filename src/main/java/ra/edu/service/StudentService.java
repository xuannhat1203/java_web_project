package ra.edu.service;

import ra.edu.entity.User;

import java.util.List;

public interface StudentService {
    List<User> listStudentPagination(String search, String sort, int page, int size);
    boolean updateStatus(int id);
    long countWithFilter(String keyword);
}
