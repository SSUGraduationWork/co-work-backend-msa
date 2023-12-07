package com.example.demo.src.file.dto.response;


import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.Feedbacks;

import com.example.demo.src.file.vo.ResponseTeamMember;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class FeedbackResponse {

    private Long feedbackId;

    private Long boardId;

    private String comment;

    private LocalDateTime createdAt;
    private Integer studentNumber;
    private String userName;

    private String pictureUrl;

    private Integer modReq;
    public FeedbackResponse() {

    }

    public FeedbackResponse(String comment) {
        this.comment = comment;
    }

    @Builder
    public FeedbackResponse (Long feedbackId, Long boardId, String comment,LocalDateTime createdAt,
                             Integer studentNumber,String userName,String pictureUrl,  Integer modReq){
        this.boardId = boardId;
        this.feedbackId=feedbackId;
        this.comment=comment;
        this.createdAt=createdAt;
        this.studentNumber=studentNumber;
        this.userName=userName;
        this.pictureUrl=pictureUrl;
        this.modReq=modReq;
    }

    public static FeedbackResponse from(Feedbacks feedbacks, Boards boards, ResponseTeamMember memberResponse) {
        return FeedbackResponse.builder()
                .feedbackId(feedbacks.getId())
                .boardId(boards.getId())
                .comment(feedbacks.getComment())
                .createdAt(feedbacks.getCreatedAt())
                .modReq(feedbacks.getModReq())
                .studentNumber(memberResponse.getStudentNumber())
                .userName(memberResponse.getName())
                .pictureUrl(memberResponse.getPictureUrl())
                .build();
    }
}



