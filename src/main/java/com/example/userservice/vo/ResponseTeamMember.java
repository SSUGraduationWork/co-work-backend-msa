package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResponseTeamMember {
    private Long id;
    private Integer studentNumber;
    private String name;
    private String pictureUrl;

}
