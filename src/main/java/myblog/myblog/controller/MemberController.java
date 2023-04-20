package myblog.myblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.member.LoginRequestDTO;
import myblog.myblog.dto.member.SignupRequestDTO;
import myblog.myblog.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignupRequestDTO signupRequestDto) {
        ResponseEntity responseEntity;
        try {
            responseEntity = memberService.signup(signupRequestDto);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody LoginRequestDTO loginRequestDto,
            HttpServletResponse response
    ) {
        ResponseEntity responseEntity;
        try {
            responseEntity = memberService.login(loginRequestDto, response);
        } catch (Exception e) {
            BasicResponseDTO basicResponseDTO = BasicResponseDTO.setBadRequest(e.getMessage());
            responseEntity = new ResponseEntity(basicResponseDTO, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
