package ra.edu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = "Tên đăng nhập không được đề trống")
    private String username;
    @NotBlank(message = "Mật khẩu không được đề trống")
    private String password;
}
