package com.group.express.config.Exception;

import io.jsonwebtoken.JwtException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // filterChain.doFilter(request, response)를 호출하여 요청을 다음 필터 또는 서블릿으로 전달합니다. 이렇게 하면 필터 체인의 다음 단계로 요청이 이동
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            setErrorResponse(response, e.getMessage());
        }
    }

    // 클라이언트에게 오류 응답 보내는 설정
    private void setErrorResponse(
            HttpServletResponse response, String message) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
