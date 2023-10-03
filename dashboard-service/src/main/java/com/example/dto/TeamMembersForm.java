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
//    public TeamMembers2 toEntity(Long teamId, Long userId) {
//        TeamMembers2 entity = new TeamMembers2();
//        entity.setTeamId(teamId);
//        entity.setUserId(userId);
//        return entity;
//    }

}
