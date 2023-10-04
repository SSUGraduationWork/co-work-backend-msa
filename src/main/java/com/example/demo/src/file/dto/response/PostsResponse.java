package com.example.demo.src.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsResponse {
    private Long board_id;
    private Long user_id;
    private String title;

    public PostsResponse(){

    }
}
