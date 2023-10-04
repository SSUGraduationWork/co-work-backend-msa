package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RequestTeamsForm {

    @JsonProperty("teamName")
    private String teamName;

}
