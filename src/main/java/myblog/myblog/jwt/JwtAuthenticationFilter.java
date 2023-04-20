package myblog.myblog.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import myblog.myblog.domain.Member;
import myblog.myblog.dto.SecurityExceptionDto;
import myblog.myblog.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더의 토큰 가져오기
        String access_token = tokenProvider.resolveToken(request, tokenProvider.ACCESS_KEY);
        String refresh_token = tokenProvider.resolveToken(request, tokenProvider.REFRESH_KEY);

        // 액세스 토큰 존재 여부 판단
        if (access_token != null) {
            // 액세스 토큰 유효성 검사
            if (tokenProvider.validateToken(access_token)) {
                setAuthentication(tokenProvider.getUserInfoFromToken(access_token));
            }
            // 토큰 만료 && 리프레시 토큰이 존재
            else if (refresh_token != null) {
                // 리프레시 토큰 검증 && 리프레시 토큰 DB에서 토큰 존재 유무 확인
                boolean isRefreshToken = tokenProvider.refreshTokenValidation(refresh_token);
                // 리프레시 토큰이 유효하고, 리프레시 토큰이 DB에 존재 여부 판단
                if (isRefreshToken) {
                    // 리프레시 토큰으로 username, Member DB에서 username을 가진 member 가져오기
                    String username = tokenProvider.getUserInfoFromToken(refresh_token);
                    Member member = memberRepository.findByUsername(username).get();
                    // 새로운 액세스 토큰 발급
                    String newAccessToken = tokenProvider.create(username, member.getRole(), "Access");
                    // 헤더에 액세스 토큰 추가
                    tokenProvider.setHeaderAccessToken(response, newAccessToken);
                    // Security context에 인증 정보 넣기
                    setAuthentication(username);
                }
            }
            // (토큰 만료 && 리프레시 토큰 만료) || 리프레시 토큰이 DB와 비교했을 때 같지 않다면
             else {
                 jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST.value());
                 return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장
    public void setAuthentication(String username) {
        Authentication authentication = tokenProvider.createAuthentication(username);
        // security가 만들어주는 securityContextHolder 그 안에 authentication을 넣음
        // security가 securitycontextholder에서 인증 객체를 확인
        // JwtAuthenticationFilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않음
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 예외 처리 핸들러
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
