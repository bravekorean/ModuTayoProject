package com.group.express.controller;

import com.group.express.domain.Member;
import com.group.express.service.MemberService;
import com.group.express.service.MileageService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/Mileage")
public class mileageController {
    private final MemberService memberService;
    private final MileageService mileageService;

    private mileageController(MemberService service, MileageService mileage) {
        this.memberService = service;
        this.mileageService = mileage;
    }

    @GetMapping("/getMileage")
    public ResponseEntity<?> getMileage(@RequestParam String id) {
        Member member = memberService.getMemberById(id);
        if (member != null) {
            try {
                int Mileage = member.getMileage();
                return ResponseEntity.ok(Mileage);
            } catch (Exception e) {
                System.out.println("마일리지 조회 작업 실패");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mileage update failed.");
            }
        } else {
            System.out.println("관련 id가 없음.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 id가 존재하지 않음.");
        }
    }

@PutMapping("/UpdateMileage")
public ResponseEntity<?> updateMileage(@RequestParam String id, @RequestParam int mileage, @RequestParam int paidAmount) {
    Member member = memberService.getMemberById(id);

    if (member != null) {
    try {
        int totalMileage = mileageService.insertMileage(mileage, paidAmount);
        member.setMileage((totalMileage));
        memberService.updateMember(member);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        System.out.println("마일리지 업데이트 작업 실패");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mileage update failed.");
    }
} else {
        System.out.println("관련 id가 없음.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 id가 존재하지 않음.");
    }

}

    @PutMapping("/rollbackMileage")
    public ResponseEntity<?> rollbackMileage(@RequestParam String id, @RequestParam int usedMileage) {
        Member member = memberService.getMemberById(id);
        int OwendMileage = member.getMileage();

        if (member != null) {
            try {
                int totalMileage = mileageService.rollbackMileage(usedMileage,OwendMileage);
                member.setMileage((totalMileage));
                memberService.updateMember(member);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                System.out.println("마일리지 환불 작업 실패");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mileage rollback failed.");
            }
        } else {
            System.out.println("관련 id가 없음.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 id가 존재하지 않음.");
        }

    }


}
