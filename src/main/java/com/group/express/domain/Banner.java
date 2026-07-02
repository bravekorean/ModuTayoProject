package com.group.express.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Banner")
public class Banner {
    @Id
    @Column(name = "banner_id")
    private Long bannerId;
    @Column(name = "banner_title")
    private String title;
    @Column(name = "banner_content")
    private String content;
    @Column(name = "banner_photo_name")
    private String bannerPhotoName;
    @Column(name = "banner_photo_size")
    private int bannerPhotoSize;
    @Column(name = "banner_photo")
    @Lob
    private byte[] bannerPhoto;







}