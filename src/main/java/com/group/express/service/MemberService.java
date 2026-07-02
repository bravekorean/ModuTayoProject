package com.group.express.service;

import com.group.express.DTO.MemberDTO;
import com.group.express.domain.Authority;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void registerUser(MemberDTO memberDto) {

        Member member = new Member();
        String encodedPassword = passwordEncoder.encode(memberDto.getPass()); // 비밀번호 암호화
        member.setId(memberDto.getId());
        member.setPass(encodedPassword);
        member.setName(memberDto.getName());
        member.setPhonenumber(memberDto.getPhonenumber());
        member.setMileage(memberDto.getMileage());
        member.setEmail(memberDto.getEmail());
        member.setAddress(memberDto.getAddress());
        member.setRole(Authority.user);
        // Set other user properties and validation if needed

        memberRepository.save(member);
    }


    public List<Member> getMemberList() {return memberRepository.findAll(); }

    public List<Member> getMemberListByName(String name) {return memberRepository.findByNameContaining(name);}

    public List<Member> getMemberListById(String id) {return memberRepository.findByIdContaining(id);}

    public Member getMemberById(String id){
        return memberRepository.findById(id).orElse(null);
    }
    public  Member vaildUser(String id) {
        return memberRepository.findById(id).orElse(null);
    }
    public Member updateMember(Member member){return memberRepository.save(member);}
    public void deleteMember(String id){memberRepository.deleteById(id);}

    public void updatePass(Member member, String newPass) {
        member.setPass(passwordEncoder.encode(newPass));
        memberRepository.save(member);
    }
}


