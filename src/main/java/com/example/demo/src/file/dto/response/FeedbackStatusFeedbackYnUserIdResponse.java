package com.example.demo.src.file.dto.response;

import lombok.Data;

@Data
public class FeedbackStatusFeedbackYnUserIdResponse {
    private Integer feedbackYn;
    private Long userId;

    public FeedbackStatusFeedbackYnUserIdResponse(Integer feedbackYn, Long userId) {
        this.feedbackYn = feedbackYn;
        this.userId = userId;
    }

    // Getter 및 Setter 메서드
}