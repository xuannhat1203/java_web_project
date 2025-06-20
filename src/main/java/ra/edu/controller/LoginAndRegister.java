package ra.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ra.edu.dto.LoginDto;
import ra.edu.entity.Login;
import ra.edu.enumData.Role;
import ra.edu.enumData.StatusAccount;
import ra.edu.dto.UserDTO;
import ra.edu.entity.User;
import ra.edu.service.PersonService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("auth")
public class LoginAndRegister {
    @Autowired
    private PersonService personService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult,  RedirectAttributes redirectAttributes) {
        if (personService.existsByUsername(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "error.userDTO", "Tên đăng nhập đã tồn tại");
        }

        if (personService.existsByEmail(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "error.userDTO", "Email đã tồn tại");
        }

        if (personService.existsByPhone(userDTO.getPhone())) {
            bindingResult.rejectValue("phone", "error.userDTO", "Số điện thoại đã tồn tại");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setName(userDTO.getName());
        user.setDob(userDTO.getDob());
        user.setSex(userDTO.getSex());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCreate_at(LocalDate.now());

        personService.register(user);

        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công!");

        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        if (!model.containsAttribute("userLogin")) {
            model.addAttribute("userLogin", new LoginDto());
        }
        return "login";
    }


    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("userLogin") LoginDto userLogin,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model,
                        HttpSession session) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("userLogin", userLogin);
            return "login";
        }

        User user = personService.login(userLogin.getUsername(), userLogin.getPassword());

        if (user != null) {
            if (user.getStatus().equals(StatusAccount.ACTIVE)) {
                session.setAttribute("user", user);

                if (user.getRole().equals(Role.ADMIN)) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/courses";
                }

            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản đang bị khóa");
                return "redirect:/auth/login";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Tài khoản không tìm thấy");
            return "redirect:/auth/login";
        }
    }


}
