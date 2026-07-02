package com.group.express.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Member")
public class Member {

    @Id
    private String id;

    private String pass;

    private String name;

    @Column(name = "phonenumber")
    private String phonenumber;
    private int mileage;

    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Authority role;




}