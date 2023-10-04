package com.example.dto;

import com.example.entity.Minutes;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class MinutesForm {
    @JsonProperty("id")
    private  Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("teamId")
    private Long teamId;
    @JsonProperty("date")
    private String date;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;

}
