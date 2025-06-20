package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.entity.Course;
import ra.edu.entity.Enrollment;
import ra.edu.entity.User;
import ra.edu.enumData.StatusEnrollment;
import ra.edu.service.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class ListCourseUserPageController {

    @Autowired
    private CourseUserService courseService;

    @Autowired
    private CourseService courseServiceImp;

    @Autowired
    private PersonService personService;

    @Autowired
    private EnrollmentUserRegisterService enrollmentUserRegisterService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public String listCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search,
            Model model, HttpSession session) {

        User user = (User) session.getAttribute("user"); // Có thể null
        model.addAttribute("user", user);

        List<Course> allCourses;
        if (search != null && !search.isEmpty()) {
            allCourses = courseService.searchCoursesByName(search);
            model.addAttribute("search", search);
        } else {
            allCourses = courseService.findAllCourses();
        }

        int totalElements = allCourses.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);
        List<Course> paginatedCourses = allCourses.subList(startIndex, endIndex);

        model.addAttribute("courses", paginatedCourses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);

        List<Integer> enrolledCourseIds = user != null
                ? enrollmentService.findEnrollmentsByUserId(user.getId())
                .stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toList())
                : List.of();
        model.addAttribute("enrolledCourseIds", enrolledCourseIds);

        return "mainPage";
    }

    @PostMapping("/{idCourse}/register")
    @ResponseBody
    public String registerCourse(@PathVariable int idCourse, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "unauthenticated";
            }

            boolean isRegistered = enrollmentUserRegisterService.checkEnrollment(user.getId(), idCourse);
            if (isRegistered) {
                return "registered";
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setCourse(courseServiceImp.findById(idCourse));
            enrollment.setUser(user);
            enrollment.setRegistered_at(LocalDateTime.now());
            enrollment.setStatus(StatusEnrollment.WAITING);

            boolean success = enrollmentUserRegisterService.registerCourse(enrollment);
            return success ? "success" : "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/{id}/details")
    @ResponseBody
    public Course getCourseDetails(@PathVariable int id) {
        return courseServiceImp.findById(id);
    }

    @PostMapping("/{id}/check-enrollment")
    @ResponseBody
    public boolean checkEnrollment(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return enrollmentUserRegisterService.checkEnrollment(user.getId(), id);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
