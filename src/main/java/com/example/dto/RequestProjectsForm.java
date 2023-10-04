package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RequestProjectsForm {
    @JsonProperty("projectName")
    private String projectName;
    @JsonProperty("semester")
    private String semester;

}
