package com.group.express.controller;

import com.group.express.DTO.TokenDTO;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import com.group.express.service.JWTProvider;
import com.group.express.service.LoginRequest;
import com.group.express.service.LoginService;
import com.group.express.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberRepository memberRepository;
    private final JWTProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoginService loginService;
    private final SearchService searchService;


    @Autowired
    public LoginController(MemberRepository memberRepository, JWTProvider jwtProvider, PasswordEncoder passwordEncoder, LoginService loginService, SearchService searchService) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
        this.loginService = loginService;
        this.searchService = searchService;
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        TokenDTO tk = loginService.login(loginRequest);
        Member member = memberRepository.findById(loginRequest.getUsername()).orElse(null);

        return ResponseEntity.ok(tk);
    }

    // 아이디 찾기
    @PostMapping("/searchId")
    @CrossOrigin("http://localhost:3000")
    public ResponseEntity<Map<String, Object>> searchId(@RequestBody Member m) {
        log.info("${}",m);
        Map<String, Object> response = new HashMap<>();
        String myId = searchService.searchId(m);
        if ("not found id".equals(myId)) {
            response.put("success", false);
            response.put("statusCode", HttpStatus.NOT_FOUND.value());
            response.put("message", "아이디 찾기 실패, 입력값을 확인해주세요\n");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("success", true);
            response.put("statusCode", HttpStatus.OK.value()); // 200 OK 상태 코드
            response.put("message", "아이디를 찾았습니다.\n");
            response.put("foundId", myId); // 아이디를 찾은 경우 아이디 값을 추가
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // 비밀번호 찾기

    @PostMapping("/searchPw")
    @CrossOrigin("http://localhost:3000")
    public ResponseEntity<String> searchPw(@RequestBody Member m) {
        String newPassword = searchService.searchPw(m);
        if (newPassword == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(newPassword);
    }

    // 카카오 로그인
    @PostMapping("/kakaoLogin")
    public ResponseEntity<TokenDTO> kakaoLogin(@RequestBody TokenDTO tokenDTO) {
        if (tokenDTO == null || !StringUtils.hasText(tokenDTO.getAccessToken())) {
            throw new JwtException("카카오 로그인 오류");
        }

        return ResponseEntity.ok(loginService.kakaoLogin(tokenDTO.getAccessToken()));
    }


}
