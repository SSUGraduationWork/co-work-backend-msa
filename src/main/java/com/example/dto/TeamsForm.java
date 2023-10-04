package com.example.dto;


import com.example.entity.Teams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TeamsForm {
    @JsonProperty("teamId")
    private  Long teamId;

    @JsonProperty("projectId")
    private Long projectId;

    @JsonProperty("teamName")
    private String teamName;
    @JsonProperty("teamNumber")
    private Long teamNumber;


}
