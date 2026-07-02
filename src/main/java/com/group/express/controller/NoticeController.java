package com.group.express.controller;

import com.group.express.domain.Member;
import com.group.express.domain.Notice;
import com.group.express.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notices")
@CrossOrigin(origins = "http://localhost:3000")
public class NoticeController {
    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getNewNotices() {
        List<Notice> notices = noticeService.getNewNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/view")
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    @GetMapping("/{num}")
    public ResponseEntity<Notice> getNotice(@PathVariable Long num) {
        Notice notice = noticeService.getNoticeById(num);
        notice.setVisitcount(notice.getVisitcount()+1);


        if (notice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( noticeService.updateNotice(notice));
    }

    @PostMapping()
    public ResponseEntity<Notice> createNotice ( @RequestParam("title") String title,
                                                 @RequestParam("content") String content,
                                                @RequestParam("postdate") String postdate,
                                                @RequestParam(value="myfile",required = false) MultipartFile file) throws IOException, SQLException {
        Notice notice=new Notice();

        notice.setTitle(title);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(postdate, formatter);
        notice.setPostdate(localDate);
        notice.setContent(content);
        if(file != null && !file.isEmpty()){
        notice.setFileSize((int)file.getSize());
        notice.setFileName(file.getOriginalFilename());
        byte[] fileBytes = file.getBytes();
        notice.setFile(fileBytes);
        }
        Notice createdNotice = noticeService.createNotice(notice);
        return ResponseEntity.ok(createdNotice);
    }

    @PutMapping("/{num}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long num,
                                               @RequestParam("title") String title,
                                               @RequestParam("content") String content,
                                               @RequestParam("postdate") String postdate,
                                               @RequestParam(value="myfile",required = false) MultipartFile file,
                                               @RequestParam("visitcount") String count) throws IOException {
        Notice notice=noticeService.getNoticeById(num);
        notice.setTitle(title);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(postdate, formatter);
        notice.setPostdate(localDate);
        notice.setContent(content);
        notice.setVisitcount(Integer.parseInt(count));
        notice.setNum(num); // 업데이트할 공지사항의 번호를 설정
        if(file != null && !file.isEmpty()){
            notice.setFileSize((int)file.getSize());
            notice.setFileName(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            notice.setFile(fileBytes);
        }
        else{
            notice.setFileName(null);
            notice.setFileSize(0);
            notice.setFile(null);
        }
        Notice updatedNotice = noticeService.updateNotice(notice);
        if (updatedNotice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNotice);
    }

    @DeleteMapping("/{num}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long num) throws IOException {
        Notice notice=noticeService.getNoticeById(num);
        noticeService.deleteNotice(num);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/SearchFilter")
    public ResponseEntity<List<Notice>> getListMember(@RequestParam(required = false) String search, @RequestParam String classification){
        List<Notice> noticeList = null;
        if(search==null||search.isEmpty()){
           noticeList=noticeService.getAllNotices();
       }else if(classification.equals("content")) {
            noticeList = noticeService.getMemberListByContent(search);
        }
        else if(classification.equals("title")) {
            noticeList = noticeService.getMemberListByTitle(search);
        }

        return ResponseEntity.ok(noticeList);
        }
}
