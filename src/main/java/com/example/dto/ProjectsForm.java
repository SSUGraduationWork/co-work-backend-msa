package com.example.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProjectsForm {
    @JsonProperty("projectId")
    private  Long projectId;

    @JsonProperty("professorId")
    private Long professorId;

    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("semester")
    private String semester;

    @JsonProperty("projectNumber")
    private Long projectNumber;


}
