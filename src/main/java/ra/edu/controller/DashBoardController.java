package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ra.edu.service.DashboardService;

import static java.lang.System.out;

@Controller
@RequestMapping("/admin/dashboard")
public class DashBoardController {

    @Autowired
    public DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", dashboardService.countTotalCourses());
        model.addAttribute("totalStudents", dashboardService.countTotalStudents());
        model.addAttribute("totalEnrollments", dashboardService.countTotalEnrollments());
        model.addAttribute("courseStats", dashboardService.getAllCourses());
        model.addAttribute("top5Courses",dashboardService.get5bestCourses());
        model.addAttribute("content", "dashboard");
        return "admin";
    }
}
