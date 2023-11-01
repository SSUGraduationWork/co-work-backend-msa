package com.example.demo.src.file.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsResponse {
    private Long id;
    private Long userId;
    private String title;

    public PostsResponse(){

    }
}
