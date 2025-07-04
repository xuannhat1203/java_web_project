package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.entity.User;
import ra.edu.enumData.Role;
import ra.edu.service.DashboardService;

import javax.servlet.http.HttpSession;

import static java.lang.System.out;

@Controller
@RequestMapping("/admin/dashboard")
public class DashBoardController {

    @Autowired
    public DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
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
        model.addAttribute("totalCourses", dashboardService.countTotalCourses());
        model.addAttribute("totalStudents", dashboardService.countTotalStudents());
        model.addAttribute("totalEnrollments", dashboardService.countTotalEnrollments());
        model.addAttribute("courseStats", dashboardService.getAllCourses());
        model.addAttribute("top5Courses",dashboardService.get5bestCourses());
        model.addAttribute("content", "dashboard");
        return "admin";
    }
}
