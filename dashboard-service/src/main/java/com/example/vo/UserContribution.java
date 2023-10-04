package com.example.vo;

import lombok.Data;

@Data
public class UserContribution {
    private Long id;
    private Integer studentNumber;
    private String name;
    private String pictureUrl;
    private Long contribution;
}
