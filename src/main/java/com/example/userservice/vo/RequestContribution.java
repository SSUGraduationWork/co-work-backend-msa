package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestContribution {
    private Long id;
    private Float contribution;
}
