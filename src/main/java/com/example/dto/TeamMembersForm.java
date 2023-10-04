package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TeamMembersForm {
    @Id
    @JsonProperty("teamMemberId")
    private Long teamMemberId;

    @JsonProperty("teamId")
    private  Long teamId;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("contribution")
    private Long contribution;


}
