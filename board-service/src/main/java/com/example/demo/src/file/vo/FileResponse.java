package com.example.demo.src.file.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class FileResponse {

        private Long id;
        private String filename;
        private String filepath;
        private Long boardId;
        @Builder
        FileResponse(Long id,String filename, String filepath,Long boardId){
                this.id=id;
                this.filename=filename;
                this.filepath=filepath;
                this.boardId=boardId;
        }
}
