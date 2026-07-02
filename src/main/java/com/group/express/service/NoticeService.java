package com.group.express.service;

import com.group.express.domain.Notice;
import com.group.express.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Autowired
    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    // 공지사항 목록 조회
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // 공지사항 상세 조회
    public Notice getNoticeById(Long num) {
        return noticeRepository.findById(num).orElse(null);
    }

    // 공지사항 등록
    public Notice createNotice(Notice notice){
        return noticeRepository.save(notice);
    }

    // 공지사항 수정
    public Notice updateNotice(Notice notice) {
        return noticeRepository.save(notice);
    }

    // 공지사항 삭제
    public void deleteNotice(Long num) {
        noticeRepository.deleteById(num);
    }

    public List<Notice> getMemberListByContent(String search) {
        return noticeRepository.findByContentContaining(search);
    }

    public List<Notice> getMemberListByTitle(String search) {
        return noticeRepository.findByTitleContaining(search);
    }

    public List<Notice> getNewNotices() {
    return noticeRepository.findNewNoticeList(PageRequest.of(0, 3));
    }
}