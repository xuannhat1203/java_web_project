package ra.edu.service;

import org.springframework.stereotype.Service;
import ra.edu.entity.User;
import ra.edu.repository.StudentRepository;
import java.util.List;
@Service
public class StudentServiceImp implements StudentService {
    public StudentRepository studentRepository;
    public StudentServiceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Override
    public List<User> listStudentPagination(String search, String sort, int page, int size) {
        return studentRepository.listStudentPagination(search, sort, page, size);
    }
    @Override
    public boolean updateStatus(int id) {
        return studentRepository.updateStatus(id);
    }
    @Override
    public long countWithFilter(String keyword) {
        return studentRepository.countWithFilter(keyword);
    }
}
