package com.group.express.service;

import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public String searchId(Member m) {
        String name = m.getName();
        String email = m.getEmail();
        Member member = memberRepository.findByNameAndEmail(name, email);

        if (member != null) {
            return member.getId();
        } else {
            return "not found id";
        }
    }

    public String searchPw(Member m) {
        String id = m.getId();
        String email = m.getEmail();
        Optional<Member> savedMember = memberRepository.findByIdAndEmail(id, email);

        // userId, email 조회 실패 및 SNS 로그인 계정일 시 null 반환
        if (savedMember.isEmpty()) {
            return null;
        }

        // 새로운 비밀번호 생성
        int passwordLength = 15;
        String newPassword = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // 영어 대소문자와 숫자 제외한 문자 필터링
                .limit(passwordLength)  // 15글자 길이 제한
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        // 비밀번호 업데이트
        Member member = savedMember.get();
        member.setPass(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        return newPassword;
    }
}
