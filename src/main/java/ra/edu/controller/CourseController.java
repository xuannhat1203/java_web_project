package ra.edu.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.edu.dto.CourseConvertDTO;
import ra.edu.dto.CourseDTO;
import ra.edu.entity.Course;
import ra.edu.service.CourseService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public String showCourseListPage(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        loadCourseListForPage(model, name, sortDirection, page, size);
        model.addAttribute("courseDTO", new CourseDTO());
        return "admin";
    }

    @PostMapping
    public String handleSaveOrUpdateCourse(@Valid @ModelAttribute("courseDTO") CourseDTO courseDTO,
                                           BindingResult bindingResult,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(defaultValue = "asc") String sortDirection,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           Model model) {

        boolean isCheck = courseDTO.getId() != null;

        if (!isCheck && courseService.existsByName(courseDTO.getName())) {
            bindingResult.rejectValue("name", "error.courseDTO", "Tên khóa học đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            loadCourseListForPage(model, name, sortDirection, page, size);
            model.addAttribute("showFormModal", true);
            return "admin";
        }

        Course oldCourse = isCheck ? courseService.findById(courseDTO.getId()) : null;
        Course course = convertToCourse(courseDTO, oldCourse);

        try {
            MultipartFile imageFile = courseDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                course.setImage(uploadResult.get("url").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi upload ảnh", e);
        }

        if (isCheck) {
            courseService.update(course);
        } else {
            courseService.create(course);
        }

        return "redirect:/admin/courses";
    }

    private void loadCourseListForPage(Model model, String name, String sortDirection, int page, int size) {
        List<Course> courses = courseService.findAll(name, sortDirection, page, size);
        long totalCourse = courseService.countWithFilter(name);
        int totalPages = (int) Math.ceil((double) totalCourse / size);

        List<CourseConvertDTO> convertedCourses = new ArrayList<>();
        for (Course course : courses) {
            convertedCourses.add(convertToCourseDTO(course));
        }

        model.addAttribute("courses", convertedCourses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("name", name);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("content", "listCourse");
    }

    @GetMapping("/delete/{id}")
    public String handleDeleteCourse(@PathVariable("id") int id) {
        courseService.delete(id);
        return "redirect:/admin/courses";
    }

    private Course convertToCourse(CourseDTO dto, Course existingCourse) {
        Course course = new Course();
        if (existingCourse != null) {
            course.setId(existingCourse.getId());
            course.setCreate_at(existingCourse.getCreate_at());
            course.setImage(existingCourse.getImage());
        } else {
            course.setCreate_at(LocalDate.now());
        }
        course.setName(dto.getName());
        course.setDuration(dto.getDuration());
        course.setInstructor(dto.getInstructor());
        return course;
    }

    private CourseConvertDTO convertToCourseDTO(Course course) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        CourseConvertDTO dto = new CourseConvertDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDuration(course.getDuration());
        dto.setInstructor(course.getInstructor());
        dto.setImage(course.getImage());
        dto.setCreate_at(formatter.format(course.getCreate_at()));
        return dto;
    }
}
