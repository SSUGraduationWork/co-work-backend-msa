package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberDto {
    private String user_email;
    private String user_name;
    private String role;
    private Integer student_number;
    private String university;
    private String department;
    private String picture_url;

    public MemberDto(){

    }
}
