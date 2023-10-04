package com.example.demo.src.file.dto.response;


import com.example.demo.src.file.domain.Boards;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BoardResponse {



private Long boardId;
private String title;

private LocalDateTime createdTime;

private Long viewCount;

private Integer feedbackYn;
private Long userId;
private Long workId;

    @Builder
    public BoardResponse(Long boardId, String title, Long viewCount,
                         LocalDateTime createdTime,
                         Integer feedbackYn, Long userId,Long workId){
        this.boardId = boardId;
        this.title = title;
        this.viewCount=viewCount;
        this.createdTime=createdTime;
        this.feedbackYn=feedbackYn;
        this.userId=userId;
        this.workId=workId;
    }

    public static BoardResponse from(Boards boards) {
        return BoardResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .viewCount(boards.getViewCnt())
                .createdTime(boards.getCreatedAt())
                .userId(boards.getUserId())
                .workId(boards.getWorkId())
                .build();
    }
}
