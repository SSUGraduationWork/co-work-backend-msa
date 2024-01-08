package com.example.demo.src.file.dto.request;


import lombok.Data;

@Data
public class BoardWriteRequest{
    private Long boardId;

    private String title;

    private String content;


//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;


}