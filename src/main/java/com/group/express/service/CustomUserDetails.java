package com.group.express.service;

import com.group.express.DTO.UserDetailsImpl;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findById(userId);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }
        return new UserDetailsImpl(member.get());
    }
}
