package com.example.demo.src.file.vo;


import lombok.Data;

@Data
public class TeamMemberResponse {

    private Long id;

    private Long teamId;

    private Long userId;

    private float contribution;

    TeamMemberResponse(){

    }
}
