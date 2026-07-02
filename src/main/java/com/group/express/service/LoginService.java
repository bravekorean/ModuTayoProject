package com.group.express.service;


import com.group.express.DTO.TokenDTO;
import com.group.express.domain.Authority;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.JSONValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final JWTProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private static final String KAKAO_URL = "https://kapi.kakao.com/v2/user/me"; // 카카오 유저정보 API

    public TokenDTO login(LoginRequest loginRequest) {
        // 사용자 아이디와 비밀번호를 포함한 UsernamePasswordAuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        // authenticationToken 인증, 인증 성공 시 authentication 객체 반환(사용자 정보, 권한정보 포함)
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);


        // 로그인 시 토큰 생성해서 반환
        return jwtTokenProvider.generateAccessTokenDto(authentication);
    }

    //  카카오 로그인
    public TokenDTO kakaoLogin(String accessToken) {
        Member member = getKakaoUserInfo(accessToken);
        Optional<Member> savedMember = memberRepository.findById(member.getId());
        if (savedMember.isEmpty()) {
            memberRepository.save(member);
        }
        return jwtTokenProvider.generateAccessTokenDto(member.getId());  // 여기서는 getUserId()가 이메일임
    }


//    // 카카오 로그인 토큰을 이용해 유효한 회원인지 검증
//    public boolean getKakaoAuth(Principal principal, String accessToken) {
//        Member member = getKakaoUserInfo(accessToken);
//        Optional<Member> savedMember = memberRepository.findById(member.getId());
//        return savedMember.map(value -> value.getId().equals(principal.getName())).orElse(false);
//    }

    // 카카오 로그인 토큰으로 카카오 로그인 정보를 받아오는 함수
    public Member getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_URL)
                .encode().build().toUri();
        ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers),
                String.class);
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(res.getBody()));
        JSONObject accountObject = (JSONObject) jsonObject.get("kakao_account");
        JSONObject profileObject = (JSONObject) accountObject.get("profile");

        String userId = String.valueOf(jsonObject.get("id"));
        String userName = String.valueOf(profileObject.get("nickname"));
        String email = String.valueOf(accountObject.get("email"));
        String encodePassword = passwordEncoder.encode(email);

        return Member.builder()
                .id(userId)
                .pass(encodePassword)
                .name(userName)
                .email(email)
                .mileage(0)
                .role(Authority.user)
                .build();
    }
}
