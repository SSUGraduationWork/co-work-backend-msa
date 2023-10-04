package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RequestForm {

    @JsonProperty("date")
    private String date;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;
}
