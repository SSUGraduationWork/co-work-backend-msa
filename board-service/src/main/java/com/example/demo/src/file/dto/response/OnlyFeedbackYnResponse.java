package com.example.demo.src.file.dto.response;


import com.example.demo.src.file.domain.FeedbackStatuses;

import com.example.demo.src.file.vo.ResponseTeamMember;
import lombok.Builder;
import lombok.Data;

@Data
public class OnlyFeedbackYnResponse {
    private Integer feedbackYn;

    private String pictureUrl;

    @Builder
    public OnlyFeedbackYnResponse(Integer feedbackYn,String pictureUrl){
        this.feedbackYn=feedbackYn;
        this.pictureUrl=pictureUrl;
    }
    public static OnlyFeedbackYnResponse from(FeedbackStatuses feedbackStatuses, ResponseTeamMember memberResponse) {
        return OnlyFeedbackYnResponse.builder()
                .feedbackYn(feedbackStatuses.getFeedbackYn())
                .pictureUrl(memberResponse.getPictureUrl())
                .build();
    }
}
