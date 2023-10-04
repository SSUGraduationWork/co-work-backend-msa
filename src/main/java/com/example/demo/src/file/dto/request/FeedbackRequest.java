package com.example.demo.src.file.dto.request;


import lombok.Data;

@Data
public class FeedbackRequest {
    private Long feedbackId;
    private String comment;
}
