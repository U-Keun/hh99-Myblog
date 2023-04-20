package myblog.myblog.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import myblog.myblog.domain.UserRoleEnum;

@Data
public class SignupRequestDTO {

    @Size(min = 4, max = 10)
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]*$")
    private String username;

    @Size(min = 8, max = 15)
    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}", message = "비밀번호는 8~15자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    private UserRoleEnum role;
}
