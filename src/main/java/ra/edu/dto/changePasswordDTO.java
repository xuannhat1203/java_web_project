package ra.edu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class changePasswordDTO {
    @NotBlank(message = "Vui lòng nhập mật khẩu cũ")
    private String oldPassword;
    @NotBlank(message = "Vui lòng nhập mật khẩu mới")
    private String newPassword;
    @NotBlank(message = "Vui lòng nhập xác nhận mật khẩu mới")
    private String confirmPassword;
}
