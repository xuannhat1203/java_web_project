package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.entity.Enrollment;
import ra.edu.entity.User;
import ra.edu.enumData.Role;
import ra.edu.repository.EnrollmentAdminRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentAdminRepository enrollmentAdminRepository;

    @GetMapping
    public String listEnrollmentAdmin(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String statusFilter,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        if (session.getAttribute("user") == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        User user = (User) session.getAttribute("user");
        if (user.getRole() != Role.ADMIN) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn không có quyền truy cập vào trang quản lý");
            session.removeAttribute("user");
            return "redirect:/auth/login";
        }
        List<Enrollment> enrollments = enrollmentAdminRepository.listEnrollmentAdmin(keyword, statusFilter, page, size);
        Long count = enrollmentAdminRepository.countEnrollments(keyword, statusFilter);
        int totalPages = (int) Math.ceil(count / (double) size);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("count", count);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("statusFilter", statusFilter);
        model.addAttribute("keyword", keyword);
        model.addAttribute("content", "enrollmentAdminPage");

        return "admin";
    }

    @PostMapping("/confirm")
    public String confirmEnrollment(@RequestParam("id") int enrollmentId,
                                    @RequestParam("status") String status,
                                    RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentAdminRepository.findById(enrollmentId);
        if (enrollment == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đăng ký.");
            return "redirect:/admin/enrollments";
        }

        if (!"WAITING".equalsIgnoreCase(String.valueOf(enrollment.getStatus()))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu đã được xử lý trước đó.");
            return "redirect:/admin/enrollments";
        }

        enrollmentAdminRepository.changeStatus(enrollmentId, status);
        return "redirect:/admin/enrollments";
    }

    @PostMapping("/cancel")
    public String cancelEnrollment(@RequestParam("id") int enrollmentId,
                                   @RequestParam("status") String status,
                                   RedirectAttributes redirectAttributes) {
        Enrollment enrollment = enrollmentAdminRepository.findById(enrollmentId);
        if (enrollment == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy đăng ký.");
            return "redirect:/admin/enrollments";
        }

        if (!"WAITING".equalsIgnoreCase(String.valueOf(enrollment.getStatus()))) {
            redirectAttributes.addFlashAttribute("errorMessage", "Yêu cầu đã được xử lý trước đó.");
            return "redirect:/admin/enrollments";
        }

        enrollmentAdminRepository.changeStatus(enrollmentId, status);
        return "redirect:/admin/enrollments";
    }

}
