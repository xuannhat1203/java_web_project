package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ra.edu.entity.Enrollment;
import ra.edu.entity.User;
import ra.edu.service.EnrollmentService;
import ra.edu.service.EnrollmentServiceImp;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Controller
public class EnrollmentUserController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/historyEnrollment")
    public String historyEnrollment(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "5") int size,
                                    @RequestParam(value = "sortStatus", required = false) String sortStatus,
                                    @RequestParam(value = "search", required = false) String search,
                                    Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Enrollment> enrollments = enrollmentService.listHistoryEnrollment(user.getId(), sortStatus, search, page, size);
        Long count = enrollmentService.countHistoryEnrollment(user.getId(), search);
        int totalPages = (int) Math.ceil(count / (double) size);

        model.addAttribute("enrollments", enrollments);
        model.addAttribute("count", count);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("sortStatus", sortStatus);
        model.addAttribute("search", search);
        return "historyEnrollment";
    }

    @PostMapping("/enrollment/cancel")
    public String cancelEnrollment(@RequestParam int enrollmentId) {
        out.println("Enrollment ID: " + enrollmentId);
        enrollmentService.cancelEnrollment(enrollmentId);
        return "redirect:/historyEnrollment";
    }
}


