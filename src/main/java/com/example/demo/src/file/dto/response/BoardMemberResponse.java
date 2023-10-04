package com.example.demo.src.file.dto.response;



import com.example.demo.src.file.vo.ResponseTeamMember;
import lombok.Builder;
import lombok.Data;


@Data
public class BoardMemberResponse {


    private Long userId;
    private String writerName;
    private String pictureUrl;
    private Integer studentNumber;
    @Builder
    public BoardMemberResponse (Long userId, String writerName,String pictureUrl,Integer studentNumber){
        this.userId=userId;
        this.writerName=writerName;
        this.pictureUrl=pictureUrl;
        this.studentNumber=studentNumber;
    }

    public static BoardMemberResponse  from(ResponseTeamMember memberResponse) {
        return BoardMemberResponse .builder()
                .userId(memberResponse.getId())
                .writerName(memberResponse.getName())
                .pictureUrl(memberResponse.getPictureUrl())
                .studentNumber(memberResponse.getStudentNumber())
                .build();
    }

}