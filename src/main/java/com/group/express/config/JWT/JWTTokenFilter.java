package com.group.express.config.JWT;

import com.group.express.service.JWTProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
// 생성자 자동생성
@Slf4j
// 로깅 코드 자동생성
public class JWTTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";  // 토큰이 포함된 요청 헤더
    public static final String BEARER_PREFIX = "Bearer";
    private final JWTProvider jwtTokenProvider;

    // Request Header에서 토큰 정보를 꺼내옴, 첫 로그인 시에는 실행X(요청헤더에 토큰이 없기때문에)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);  // 요청헤더 중 Authorization 헤더

        // StringUtils.hasText(arg) : 파라미터가 문자열인지 확인
        // 문자열이고 Bearer로 시작하면 토큰 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // Ex) Bearer xxxxx~~ -> xxxxx~~ (7번째 문자열부터 리턴)
        }
        return null;
    }

    /*
    필터링을 실행하는 메소드
    resolveToken 메소드를 통해 토큰 정보를 꺼내오고, validateToken 메소드로 토큰의 유효성 검사 ->
    유효하다면 Authentication을 가져와서 SecurityContext에 저장 ->
    SecurityContext에서 허가된 uri 이외의 모든 Request 요청은 이 필터를 거치며, 토큰 정보가 없거나 유효하지 않으면 정상적으로 수행되지 않음.
    반대로 Request가 정상적으로 Controller까지 도착했으면, SecurityContext에 Member ID가 존재한다는 것이 보장
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = resolveToken(request);  // 요청헤더에서 토큰 추출

        // 문자열이고 유효성검사 통과하면 이 엑세스 토큰을 매개변수로 보내 JWT 토큰에서 사용자 정보와 권한 정보를 추출하여
        // Authentication에 넣어 객체 생성
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

            // Authentication -> SecurityContext -> SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication); // Authentication 객체를 SecurityContestHolder에 저장
        }
        filterChain.doFilter(request, response); // FilterChain으로 연결하여 준다.
    }


}
