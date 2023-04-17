package myblog.myblog.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequestDTO {

    @Size(min = 4, max = 10)
    @Pattern(regexp = "^[a-z0-9]*$" , message = "아이디 형식이 올바르지 않습니다.")
    private String username;

    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9]*$" , message = "비밀번호 형식이 올바르지 않습니다.")
    private String password;
}
