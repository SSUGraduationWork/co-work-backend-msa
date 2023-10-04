package com.example.vo;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class MemberResponse {


    private Long id;
    private String userName;
    private String role;
    private Integer studentNumber;
    private String university;
    private String department;
    private String pictureUrl;
    private String userEmail;
    private Timestamp createdAt;

}
