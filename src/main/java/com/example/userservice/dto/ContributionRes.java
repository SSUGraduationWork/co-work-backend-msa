package com.example.userservice.dto;

import lombok.Data;

@Data
public class ContributionRes {
    private Long id;
    private Integer studentNumber;
    private String name;
    private String pictureUrl;
    private Long contribution;

}
