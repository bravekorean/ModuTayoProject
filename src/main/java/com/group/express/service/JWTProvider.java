package com.group.express.service;

import com.group.express.DTO.TokenDTO;
import com.group.express.config.JWT.expiredTime;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private final Key key;
    private final UserDetailsService userDetailsService;
    private final MemberRepository memberRepository;



    // 생성자
    public JWTProvider(@Value("${JWTsecretKey}") String secretKey, UserDetailsService userDetailsService, MemberRepository memberRepository)  {
        this.userDetailsService = userDetailsService;  // userDetailsService 주입
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);  // secretKey를 디코딩해 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);  // 위에서 디코딩한 바이트 배열의 secretKey를 기반으로 실제 사용하는 서명키 생성
        this.memberRepository = memberRepository;
    }


    // Token 생성
    public TokenDTO generateAccessTokenDto(Authentication authentication) {  // 매개변수 : 로그인 유저의 정보
        // 권한을 받음. Ex) authorities = ROLE_USER
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 현재 시간
        long now = new Date().getTime();

        // Token 만료 시간 설정 : 현재 시간 + security패키지의 ExpireTime에 지정한 ACCESS_TOKEN_EXPIRE_TIME 시간
        Date accessTokenExpiresIn = new Date(now + expiredTime.ACCESS_TOKEN_EXPIRE_TIME.getTime());
        log.info(String.valueOf(accessTokenExpiresIn));
        // 인증된 사용자의 정보를 들고온다. 
        Member member = memberRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + authentication.getName()));
        // Token 생성
        String accessToken = Jwts.builder()  // JWT 생성을 위한 빌더 객체 생성
                .setSubject(authentication.getName())  // 사용자 아이디
                .claim(AUTHORITIES_KEY,  // generateAccessTokenDto에서 찾은 authorities
                        authorities) // Custom Claim 지정, Claims는 JWT의 body이고 JWT 생성자가 JWT를 받는이들이게 제시하기 바라는 정보를 포함
                // 추가 정보를 끼워넣는다.
                .claim("name", member.getName()) // 이름
                .claim("phonenumber", member.getPhonenumber()) // 휴대폰번호
                .claim("mileage",member.getMileage()) // 마일리지
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발생시간 : 현재 시간
                .setExpiration(accessTokenExpiresIn) // 토큰 만료시간 : 47라인에서 설정한 만료시간
                .signWith(key, SignatureAlgorithm.HS512) // sign key 지정, JWT 서명
                .compact();  // JWT 빌더에서 설정된 정보를 텍스트 형식의 문자열로 표현 : 버와 클라이언트 간에 정보를 안전하게 교환하는 데 사용

        // TokenDto에 생성한 Token의 정보를 넣는다
        return TokenDTO.builder()  // AcessTokentDto에 값을 넣어 객체로 만듬
                .grantType(BEARER_TYPE)  // JWT 액세스 토큰의 타입 : 일반적으로 bearer
                .accessToken(accessToken)  // 위에서 생성한 문자열인 accessToken
                .tokenExpiresIn(accessTokenExpiresIn.getTime())  // 토큰의 만료시간
                .build();  // 최종적으로 객체를 만들어 생성, 반환
    }

    public TokenDTO generateAccessTokenDto(String email) {  // 카카오 로그인 시
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + expiredTime.ACCESS_TOKEN_EXPIRE_TIME.getTime());

        Member member = memberRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        String accessToken = Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY,
                        "user") // Custom Claim 지정, Claims는 JWT의 body이고 JWT 생성자가 JWT를 받는이들이게 제시하기 바라는 정보를 포함
                .claim("sns","Kakao")
                .claim("name", member.getName()) // 이름
                .claim("phonenumber", member.getPhonenumber()) // 휴대폰번호
                .claim("mileage",member.getMileage()) // 마일리지
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발생시간 : 현재 시간
                .setExpiration(accessTokenExpiresIn) // 만료시간
                .signWith(key, SignatureAlgorithm.HS512) // sign key 지정
                .compact();

        return TokenDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(accessTokenExpiresIn.getTime())
                .build();
    }


    // Token을 받았을 때 Token의 인증을 꺼내는 메소드
    // JWT (JSON Web Token) 액세스 토큰에서 사용자 정보와 권한 정보를 추출하고,
    // 이를 기반으로 Spring Security의 Authentication 객체를 생성하는 부분
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {    // JWT 토큰에 권한(ROLE_USER, ROLE_ADMIN)이 없으면 런타임에러 발생시킴
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        String username = getUserName(accessToken);  // 엑세스 토큰에서 .setSubject(authentication.getName())로 설정한 값
        // username에 대입, 여기서는 유저 아이디

        // 위에서 username에 대입한 값으로 db에서 검색해 사용자 정보를 조회 후 UserDetails 객체로 반환(사용자정보, 권한정보 담고 있음)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 첫번째 매개변수 : 위에서 조회한 UserDetails, 두번째 매개변수 : 빈 문자열, 세번째 매개변수 : 조회한 사용자의 권한정보
        // UsernamePasswordAuthenticationToken() 객체는 사용자의 인증 및 권한부여를 관리, 인증된 사용자를 나타냄
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Token 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new JwtException("해당 토큰은 ACCESS TOKEN이 아닙니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new JwtException("해당 토큰은 유효한 토큰이 아닙니다.");
        }
    }

    // token을 매개변수로 받아 유저 아이디 반환
    public String getUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // accessToken을 매개변수로 받아 클레임 정보 반환
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts
                    .parserBuilder() // JwtParseBuilder 인스턴스 생성
                    .setSigningKey(key) // JWT 서명 검증을 위한 키 설정
                    .build()
                    .parseClaimsJws(accessToken)  // JWT 토큰의 서명을 검증하고 토큰의 클레임 (claim) 정보를 추출
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();  // 만료된 토큰의 클레임값
        }
    }

}
