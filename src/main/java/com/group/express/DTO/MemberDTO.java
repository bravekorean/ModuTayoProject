package com.group.express.DTO;

import com.group.express.domain.Authority;

import javax.persistence.Id;

public class MemberDTO {

    public MemberDTO() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Authority getRole() {
        return role;
    }

    public void setRole(Authority role) {
        this.role = role;
    }
    @Id
    private String id;
    private String pass;
    private String name;
    private String phonenumber;

    private int mileage;

    private String email;

    private String address;

    private Authority role;

    public MemberDTO(String id, String pass, String name, String phonenumber, int mileage, String email, String address, Authority role) {
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.phonenumber = phonenumber;
        this.mileage = mileage;
        this.email = email;
        this.address = address;
        this.role = role;
    }



}
