package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Boards;
import lombok.Builder;
import lombok.Data;


@Data
public class OneBoardIdResponse {



    private Long boardId;
    private boolean feedbackYn;


    @Builder
    public OneBoardIdResponse(Long boardId,boolean feedbackYn){
        this.boardId = boardId;
        this.feedbackYn=feedbackYn;
    }

    public static OneBoardIdResponse from(Boards boards) {
        return OneBoardIdResponse.builder()
                .boardId(boards.getId())
                .feedbackYn(boards.isFeedbackYn())
                .build();
    }
}
