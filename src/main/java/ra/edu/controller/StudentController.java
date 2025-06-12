package ra.edu.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ra.edu.dto.UserDTO;
import ra.edu.entity.User;
import ra.edu.enumData.Role;
import ra.edu.enumData.StatusAccount;
import ra.edu.service.StudentServiceImp;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class StudentController {
    public StudentServiceImp studentServiceImp;

    public StudentController(StudentServiceImp studentServiceImp) {
        this.studentServiceImp = studentServiceImp;
    }

    @GetMapping
    public String showStudentListPage(@RequestParam(required = false) String name,
                                      @RequestParam(defaultValue = "asc") String sortDirection,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      Model model) {
        loadStudentListForPage(model, name, sortDirection, page, size);
        return "admin";
    }

    private void loadStudentListForPage(Model model, String name, String sortDirection, int page, int size) {
        List<User> students = studentServiceImp.listStudentPagination(name, sortDirection, page, size);
        long totalStudents = studentServiceImp.countWithFilter(name);
        int totalPages = (int) Math.ceil((double) totalStudents / size);
        model.addAttribute("users", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
        model.addAttribute("size", size);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("content", "listStudent");
    }

    private User convertToUser(UserDTO dto, User existingUser) {
        User user = new User();
        if (existingUser != null) {
            user.setId(existingUser.getId());
            user.setCreate_at(existingUser.getCreate_at());
            user.setRole(existingUser.getRole());
            user.setStatus(existingUser.getStatus());
            user.setEnrollments(existingUser.getEnrollments());
        } else {
            user.setCreate_at(LocalDate.now());
            user.setRole(Role.STUDENT);
            user.setStatus(StatusAccount.ACTIVE);
        }
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setDob(dto.getDob());
        user.setSex(dto.getSex());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setDob(user.getDob());
        dto.setSex(user.isSex());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }


}
