package com.group.express.controller;

import com.group.express.domain.Banner;
import com.group.express.domain.Notice;
import com.group.express.service.BannerService;
import com.group.express.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/banner")
@CrossOrigin(origins = "http://localhost:3000")
public class bannerController {
    private final BannerService bannerService;

    @Autowired
    public bannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PutMapping("/edit")
    public ResponseEntity<Banner> updateNotice(@RequestParam("title") String title,
                                               @RequestParam("content") String content,
                                               @RequestParam("number") int number,
                                               @RequestParam(value="myfile",required = false) MultipartFile file
                                               ) throws IOException {
        Banner banner = new Banner();
        Optional<Banner> optionalbanner = bannerService.getBannerById(number);
        if(optionalbanner.isPresent()){
            banner=optionalbanner.get();
        }
        banner.setBannerId((long) number);
        banner.setTitle(title);
        banner.setContent(content);
         if(file != null && !file.isEmpty()){
            banner.setBannerPhotoSize((int)file.getSize());
            banner.setBannerPhotoName(file.getOriginalFilename());
            byte[] fileBytes = file.getBytes();
            banner.setBannerPhoto(fileBytes);
        }
        else{
             banner.setBannerPhotoSize(0);
             banner.setBannerPhotoName(null);
             banner.setBannerPhoto(null);
        }

        return ResponseEntity.ok(bannerService.saveBanner(banner));
    }

    @GetMapping("/getbannerList")
    public ResponseEntity<List<Banner>> getListBanner(){
        return ResponseEntity.ok(bannerService.getBannerList());
    }

}
