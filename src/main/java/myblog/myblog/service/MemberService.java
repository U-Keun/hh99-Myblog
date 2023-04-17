package myblog.myblog.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.StatusCode;
import myblog.myblog.dto.LoginRequestDTO;
import myblog.myblog.dto.MessageDTO;
import myblog.myblog.dto.SignupRequestDTO;
import myblog.myblog.jwt.JwtUtil;
import myblog.myblog.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MessageDTO signup(SignupRequestDTO requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 회원 중복 확인
        Optional<Member> found = memberRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Member user = new Member(username, password);
        memberRepository.save(user);
        return new MessageDTO(StatusCode.OK, "signup success", null);
    }

    public MessageDTO login(LoginRequestDTO requestDTO, HttpServletResponse response) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        //회원 체크
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 체크
        if(!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //응답 헤더에 토큰 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getUserName()));
        return new MessageDTO(StatusCode.OK, "login success", null);
    }
}
