package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Feedbacks;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardFeedbackResponse  {

    private Long feedbackId;


    private String comment;

    private LocalDateTime createdAt;
    private Long userId;

    /*
    private String userName;

    private String pictureUrl;
*/
    private Integer modReq;


    public BoardFeedbackResponse() {

    }

    public BoardFeedbackResponse(String comment,Integer modReq) {
        this.comment = comment;
        this.modReq=modReq;
    }

    @Builder
    public BoardFeedbackResponse (Long feedbackId, String comment,LocalDateTime createdAt,
                             Long userId,Integer modReq){

        this.feedbackId=feedbackId;
        this.comment=comment;
        this.createdAt=createdAt;
        this.userId=userId;
        this.modReq=modReq;
    }

    public static BoardFeedbackResponse from(Feedbacks feedbacks) {
        return BoardFeedbackResponse.builder()
                .feedbackId(feedbacks.getId())
                .comment(feedbacks.getComment())
                .createdAt(feedbacks.getCreatedAt())
                .userId(feedbacks.getWriterId())
                .modReq(feedbacks.getModReq())
                .build();
    }
}

