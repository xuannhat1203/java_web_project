package ra.edu.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.UserDTO;
import ra.edu.entity.User;
import ra.edu.enumData.Role;
import ra.edu.enumData.StatusAccount;
import ra.edu.service.StudentServiceImp;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

import static java.lang.System.out;

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
                                      Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("user") == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
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
    @PostMapping("/toggle-status")
    public String toggleUserStatus(@RequestParam("userId") int userId, RedirectAttributes redirectAttributes) {
        boolean result = studentServiceImp.updateStatus(userId);
        if (result) {
            redirectAttributes.addFlashAttribute("message", "Cập nhật trạng thái thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sinh viên hoặc cập nhật thất bại.");
        }
        return "redirect:/admin/users";
    }
}