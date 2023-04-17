package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.LoginRequestDTO;
import myblog.myblog.dto.MessageDTO;
import myblog.myblog.dto.SignupRequestDTO;
import myblog.myblog.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDTO signupRequestDto) {
        MessageDTO messageDTO = memberService.signup(signupRequestDto);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO loginRequestDto, HttpServletResponse response) {
        MessageDTO messageDTO =memberService.login(loginRequestDto, response);
        return new ResponseEntity(messageDTO, HttpStatus.OK);
    }
}
