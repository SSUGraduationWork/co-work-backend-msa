package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoRes {
    private Long id;
    private String name;
    private int studentNumber;
    private String university;
    private String department;
    private String pictureUrl;
    public UserInfoRes(){

    }
}
