package com.group.express.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
   private String password;


    // UsernamePasswordAuthenticationToken은 스프링 시큐리티에서 사용자의 인증을 나타내는 토큰 객체
    // 이 토큰을 사용하여 사용자의 아이디와 비밀번호를 인증 프로세스에 제공하고, 스프링 시큐리티가 이를 처리하여 사용자를 인증
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);

    }
}

