package myblog.myblog.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import myblog.myblog.domain.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    @Value("${jwt.secret.key}")
    private String SECURITY_KEY;
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Date exprTime = (Date) Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); //60 * 1분 = 1시간

    // JWT 생성하는 메서드
    public String create (String username, UserRole role) {
        Date date = new Date();
//        // 만료날짜를 현재 날짜 + 1시간으로 설정
//        Date exprTime = (Date) Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        // JWT를 생성(Bearer Prefix를 넣어야 하는지?)
        return BEARER_PREFIX +
                Jwts.builder()
                // 암호화에 사용될 알고리즘, 키
                .signWith(SignatureAlgorithm.HS512, SECURITY_KEY)
                .claim(AUTHORIZATION_KEY, role) //auth 키에 사용자 권한 value 담기
                .setSubject(username)
                .setIssuedAt(date)
                .setExpiration(exprTime)
                .compact();
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        //Authorization 이라는 헤더 값(토큰)을 가져옴
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        //토큰 값이 있는지, 토큰 값이 Bearer 로 시작하는지 판단
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //Bearer를 자른 값을 전달
            return bearerToken.substring(7);
        }
        return null;
    }

/*    // 토큰 검증(JwtUtil 에 있던 것
    public boolean validateToken(String token) {
        try {
            //토큰 검증 (내부적으로 해준다)
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }*/

    // JWT 검증
    public String validate (String token) {
        // 매개변수로 받은 token을 키를 사용해서 복호화 (디코딩)
        Claims claims = Jwts.parser().setSigningKey(SECURITY_KEY).parseClaimsJws(token).getBody();
        // 복호화된 토큰의 payload에서 제목을 가져옴
        return claims.getSubject();
    }

}
