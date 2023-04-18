package myblog.myblog.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myblog.myblog.domain.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; //60 * 1분 = 1시간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //HS256 암호화 알고리즘 사용

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
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

    // 토큰 생성
    public String createToken(String username, UserRole role) {
        Date date = new Date();

        //토큰 앞은 Bearer이 붙음
        //String 형식의 jwt토큰으로 반환됨
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) //subject라는 키에 username 넣음
                        .claim(AUTHORIZATION_KEY, role) //auth 키에 사용자 권한 value 담기
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //(현재시간 + 1시간)토큰 유효기간 지정
                        .setIssuedAt(date) //언제 토큰이 생성 되었는가
                        .signWith(key, signatureAlgorithm) //생성한 key 객체와 key객체를 어떤 알고리즘을 통해 암호화 할건지 지정
                        .compact();
    }

    // 토큰 검증
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
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        //앞에서 이미 validateToken으로 검증했다고 가정 -> try-catch가 없음
        //검증 하고, 마지막에 getBody()를 통해서 안에 들어있는 정보를 가져옴
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}