package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.changePasswordDTO;
import ra.edu.entity.User;
import ra.edu.service.PersonService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class UpdateProfileController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public String updateProfile(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        if (!model.containsAttribute("changePasswordDTO")) {
            model.addAttribute("changePasswordDTO", new changePasswordDTO());
        }

        return "profileUser";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("changePasswordDTO", new changePasswordDTO());
        model.addAttribute("showPasswordModal", true);

        return "profileUser";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePasswordDTO") changePasswordDTO changePasswordDTO,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("showPasswordModal", true);
            return "profileUser";
        }

        if (!user.getPassword().equals(changePasswordDTO.getOldPassword())) {
            bindingResult.rejectValue("oldPassword", "error.oldPassword", "Mật khẩu cũ không đúng");
            model.addAttribute("user", user);
            model.addAttribute("showPasswordModal", true);
            return "profileUser";
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Xác nhận mật khẩu không khớp");
            model.addAttribute("user", user);
            model.addAttribute("showPasswordModal", true);
            return "profileUser";
        }

        user.setPassword(changePasswordDTO.getNewPassword());
        personService.updatePassword(user.getId(), changePasswordDTO.getNewPassword());
        session.setAttribute("user", user);

        redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công");
        return "redirect:/profile";
    }

    @PostMapping("/update")
    public String updateProfileInfo(@Valid @ModelAttribute("user") User updatedUser,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập");
            return "redirect:/auth/login";
        }
        if (updatedUser.getPhone() == null || updatedUser.getPhone().trim().isEmpty()) {
            bindingResult.rejectValue("phone", "error.phone", "Vui lòng nhập số điện thoại");
        }

        if (updatedUser.getName() == null || updatedUser.getName().trim().isEmpty()) {
            bindingResult.rejectValue("name", "error.name", "Vui lòng nhập tên");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", updatedUser);
            return "profileUser";
        }
        List<User> users = personService.getAllUsers();
        for (User user : users) {
            if (user.getId() != (currentUser.getId()) && user.getPhone().equals(updatedUser.getPhone())) {
                bindingResult.rejectValue("phone", "error.phone", "Số điện thoại đã có người sử dụng");
                model.addAttribute("user", updatedUser);
                return "profileUser";
            }
        }

        currentUser.setName(updatedUser.getName());
        currentUser.setPhone(updatedUser.getPhone());
        boolean success = personService.updateProfile(currentUser);
        if (!success) {
            bindingResult.reject("updateError", "Cập nhật thất bại. Vui lòng thử lại.");
            model.addAttribute("user", updatedUser);
            return "profileUser";
        }

        session.setAttribute("user", currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công");
        return "redirect:/profile";
    }
}
