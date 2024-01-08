package com.example.demo.src.file.vo;

import lombok.Data;

@Data
public class FeedbackStatusResponse {

    private Long Id;
    private Long userId;
    private Long boardId;
    private Long teamId;
    private Integer feedbackYn;
}
