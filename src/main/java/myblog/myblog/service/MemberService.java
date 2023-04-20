package myblog.myblog.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import myblog.myblog.domain.Member;
import myblog.myblog.domain.RefreshToken;
import myblog.myblog.domain.UserRoleEnum;
import myblog.myblog.dto.TokenDTO;
import myblog.myblog.dto.member.LoginRequestDTO;
import myblog.myblog.dto.BasicResponseDTO;
import myblog.myblog.dto.member.MemberResponseDTO;
import myblog.myblog.dto.member.SignupRequestDTO;
import myblog.myblog.exception.custom_exeption.MemberException;
import myblog.myblog.repository.MemberRepository;
import myblog.myblog.jwt.TokenProvider;
import myblog.myblog.repository.RefreshTokenRepository;
import myblog.myblog.util.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원가입
     */
    public ResponseEntity signup(SignupRequestDTO requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        // 회원 중복 확인
        validateDuplicateMember(username);

        Member member = new Member(username, password, role);
        memberRepository.save(member);
        BasicResponseDTO<MemberResponseDTO> basicResponseDTO = BasicResponseDTO.setSuccess("signup success", new MemberResponseDTO(member));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    /**
     * 로그인
     */
    @Transactional(readOnly = true)
    public ResponseEntity login(LoginRequestDTO requestDTO, HttpServletResponse response) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();

        //회원 여부 체크
        Member member = validateMember(username);

        //비밀번호 체크
        validatePassword(password, member);

        //username (ID) 정보로 Token 생성
        TokenDTO tokenDto = tokenProvider.createAllToken(requestDTO.getUsername(), member.getRole());

        //Refresh 토큰 있는지 확인
        refreshTokenRepository.findByUsername(requestDTO.getUsername());



        //응답 헤더에 토큰 추가
        setHeader(response, tokenDto);
        BasicResponseDTO<MemberResponseDTO> basicResponseDTO = BasicResponseDTO.setSuccess("login success", new MemberResponseDTO(member));
        return new ResponseEntity(basicResponseDTO, HttpStatus.OK);
    }

    //헤더 설정
    private void setHeader(HttpServletResponse response, TokenDTO tokenDto) {
        response.addHeader(tokenProvider.ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(tokenProvider.REFRESH_KEY, tokenDto.getRefreshToken());
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
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ExceptionMessage.NOT_MATCHING_PASSWORD_EXCEPTION.getMessage());
        }
    }
}
