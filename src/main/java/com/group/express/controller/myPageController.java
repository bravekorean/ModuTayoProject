package com.group.express.controller;

import com.group.express.DTO.Password_checkDTO;
import com.group.express.domain.Authority;
import com.group.express.domain.Member;
import com.group.express.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class myPageController {
    private final MemberService memberService;

    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private myPageController(MemberService memberService, PasswordEncoder passwordEncoder)
    {this.memberService=memberService;
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;}


    @GetMapping("/EditMember/{id}")
    public ResponseEntity<Member> getMember(@PathVariable String id){
        Member member=memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }
    @PutMapping("/EditMember/{id}")
    public ResponseEntity<Object> putMember(@PathVariable String id,@RequestBody Member Newmember){
        Member Oldmember=memberService.getMemberById(id);
        if(passwordEncoder.matches(Newmember.getPass(), Oldmember.getPass())) {
            Oldmember.setAddress(Newmember.getAddress());
            Oldmember.setPhonenumber(Newmember.getPhonenumber());
            Oldmember.setEmail(Newmember.getEmail());
            memberService.updateMember(Oldmember);
            return ResponseEntity.ok(Oldmember);  // 200 OK with the updated member
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Password mismatch");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);  // 400 Bad Request with error message
        }
    }
    @DeleteMapping("/DeleteMember/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id,@RequestParam String password){
        Member member=memberService.getMemberById(id);
        if(passwordEncoder.matches(password,member.getPass())){
            memberService.deleteMember(id);
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/updateMember/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id,@RequestBody Password_checkDTO checkPass){
        Member member=memberService.getMemberById(id);
        if(passwordEncoder.matches(checkPass.getOldPass(),member.getPass())&&checkPass.getNewPass().equals(checkPass.getNewPass_confirm())){
            memberService.updatePass(member,checkPass.getNewPass());
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/AdminMember")
    public ResponseEntity<List<Member>> getListMember(){
        List<Member> memberList=memberService.getMemberList();
        return ResponseEntity.ok(memberList);
    }
    @GetMapping("/AdminMember/SearchFilter")
    public ResponseEntity<List<Member>> getListMember(@RequestParam(required = false) String search, @RequestParam String classification){
        List<Member> memberList = null;
        if(search==null||search.isEmpty()){
            memberList=memberService.getMemberList();
        }else if(classification.equals("name")) {
            memberList = memberService.getMemberListByName(search);
        }
        else if(classification.equals("id")) {
            memberList = memberService.getMemberListById(search);
        }

        return ResponseEntity.ok(memberList);
    }

    @GetMapping("/AdminMember_dialog/{id}")
    public ResponseEntity<Authority> getAdminMember(@PathVariable String id){
        Member member=memberService.getMemberById(id);
        return ResponseEntity.ok(member.getRole());
    }

    @DeleteMapping("/AdminMember/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable String id){
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/AdminMember/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable String id){
        Member member=memberService.getMemberById(id);
        if(member.getRole().equals(Authority.admin)){
            member.setRole(Authority.user);
        }else{
            member.setRole(Authority.admin);
        }
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    }
}
