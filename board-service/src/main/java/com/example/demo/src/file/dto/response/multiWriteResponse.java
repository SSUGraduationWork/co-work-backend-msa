package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Boards;
import lombok.Builder;
import lombok.Data;

@Data
public class multiWriteResponse {
    private Long boardId;

    @Builder
    public multiWriteResponse (Long boardId){
        this.boardId=boardId;
    }

    public static multiWriteResponse from(Boards boards) {
        return multiWriteResponse.builder()
                .boardId(boards.getId())
                .build();
    }
}
