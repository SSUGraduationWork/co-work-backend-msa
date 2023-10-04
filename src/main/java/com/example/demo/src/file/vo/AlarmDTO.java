package com.example.demo.src.file.vo;

import lombok.Data;

@Data
public class AlarmDTO {
    private String writerPictureUrl;
    private Long userId;
    private String content;
    private String redirectUrl;
    private String alarmKind;
    private Long boardId;
    private Long writerId;

}
