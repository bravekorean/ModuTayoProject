package com.group.express.controller;

import com.group.express.DTO.MemberDTO;
import com.group.express.domain.Member;
import com.group.express.repository.MemberRepository;
import com.group.express.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
public class RegisterController {
    @Autowired
    private MemberService memberservice;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @PostMapping("/Register")
    public ResponseEntity<String> registerUser(@RequestBody MemberDTO memberDTO) {
        memberservice.registerUser(memberDTO);
        return ResponseEntity.ok("Register success");
    }

    @GetMapping("/vaildRegister")
    public ResponseEntity<?> vaildRegister(@RequestParam String id) {
        Member member = memberservice.vaildUser(id);

        if (member == null) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }

    }

}
