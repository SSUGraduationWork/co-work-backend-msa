package com.example.demo.src.file.vo;


import lombok.Data;

@Data
public class TeamResponse {

    private Long id;
    private Long projectId;
    private String teamName;
    private Integer teamNumber;
}
