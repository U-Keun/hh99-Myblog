package myblog.myblog.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.StatusCode;
import myblog.myblog.dto.LoginRequestDTO;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.SignupRequestDTO;
import myblog.myblog.jwt.JwtUtil;
import myblog.myblog.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity signup(SignupRequestDTO requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 회원 중복 확인
        validateDuplicateMember(username);

        Member user = new Member(username, password);
        memberRepository.save(user);
        BasicResponseDTO basicResponseDTO = new BasicResponseDTO(StatusCode.OK, "signup success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    public ResponseEntity login(LoginRequestDTO requestDTO, HttpServletResponse response) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        //회원 체크
        Member member = validateMember(username);

        //비밀번호 체크
        validatePassword(password, member);

        //응답 헤더에 토큰 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUsername()));
        BasicResponseDTO basicResponseDTO = new BasicResponseDTO(StatusCode.OK, "login success", null);
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //회원 여부 체크
    private Member validateMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 회원이 없습니다.")
        );
    }

    //Id 중복 체크
    private void validateDuplicateMember(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(m -> {
                    throw new IllegalArgumentException("중복된 ID가 존재합니다.");
                });
    }

    //비밀번호 일치 여부 체크
    private void validatePassword(String password, Member member) {
        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
