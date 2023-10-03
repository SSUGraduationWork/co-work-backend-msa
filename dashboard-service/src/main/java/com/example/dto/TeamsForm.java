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


    public Teams toEntity(TeamsForm dto) {
        Teams entity = new Teams();
        entity.setTeamId(dto.getTeamId());
        entity.setProjectId(dto.getProjectId());
        entity.setTeamName(dto.getTeamName());
        entity.setTeamNumber(dto.getTeamNumber());
        return entity;
    }
}
