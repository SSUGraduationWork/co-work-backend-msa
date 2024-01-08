package com.example.demo.src.file.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FeedbackStatusTeamMember {
    private Long userId;
    private String userName;

    private String pictureUrl;

    private  Integer studentNumber;

    @Builder
    public FeedbackStatusTeamMember(Long userId,String userName, String pictureUrl,Integer studentNumber){
        this.userId=userId;
        this.userName=userName;
        this.pictureUrl=pictureUrl;
        this.studentNumber=studentNumber;
    }
}
