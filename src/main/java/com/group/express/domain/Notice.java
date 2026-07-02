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
@Table(name = "Notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private String title;
    private String content;
    private int visitcount;
    private LocalDate postdate;
    @Column(name = "fileName")
    private String fileName;
    @Column(name = "fileSize")
    private int fileSize;
    @Lob
    private byte[] file;






}