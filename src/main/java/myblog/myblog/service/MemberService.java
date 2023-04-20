package myblog.myblog.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.UserRole;
import myblog.myblog.dto.member.LoginRequestDTO;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.member.MemberResponseDTO;
import myblog.myblog.dto.member.SignupRequestDTO;
import myblog.myblog.exception.MemberException;
import myblog.myblog.repository.MemberRepository;
import myblog.myblog.security.TokenProvider;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity signup(SignupRequestDTO requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        UserRole role = requestDto.getRole();

        // 회원 중복 확인
        validateDuplicateMember(username);

        Member member = new Member(username, password, role);
        memberRepository.save(member);
        BasicResponseDTO<MemberResponseDTO> basicResponseDTO = BasicResponseDTO.setSuccess("signup success", new MemberResponseDTO(member));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    public ResponseEntity login(LoginRequestDTO requestDTO, HttpServletResponse response) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        //회원 체크
        Member member = validateMember(username);

        UserRole role = member.getRole();

        //비밀번호 체크
        validatePassword(password, member);

        //응답 헤더에 토큰 추가
        response.addHeader(tokenProvider.AUTHORIZATION_HEADER, tokenProvider.create(member.getUsername(), role));
        BasicResponseDTO<MemberResponseDTO> basicResponseDTO = BasicResponseDTO.setSuccess("login success", new MemberResponseDTO(member));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //회원 여부 체크
    private Member validateMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new MemberException(ExceptionMessage.NO_SUCH_MEMBER_EXCEPTION.getMessage())
        );
    }

    //Id 중복 체크
    private void validateDuplicateMember(String username) {
        memberRepository.findByUsername(username)
                .ifPresent(m -> {
                    throw new MemberException(ExceptionMessage.DUPLICATE_ID_EXCEPTION.getMessage());
                });
    }

    //비밀번호 일치 여부 체크
    private void validatePassword(String password, Member member) {
        if (!member.getPassword().equals(password)) {
            throw new MemberException(ExceptionMessage.NOT_MATCHING_PASSWORD_EXCEPTION.getMessage());
        }
    }
}
