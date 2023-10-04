package com.example.vo;



import com.example.entity.TeamMembers;
import lombok.Builder;
import lombok.Data;

@Data
public class TeamMemberResponse {

    private Long id;

    private Long teamId;

    private Long userId;

    private float contribution;

    TeamMemberResponse(){

    }

    @Builder
    public TeamMemberResponse(Long id, Long teamId, Long userId, float contribution){
        this.id=id;
        this.teamId=teamId;
        this.userId=userId;
        this.contribution=contribution;
    }

    public static TeamMemberResponse from(TeamMembers teamMembers) {
        return TeamMemberResponse.builder()
                .id(teamMembers.getTeamMemberId())
                .teamId(teamMembers.getTeamId())
                .userId(teamMembers.getUserId())
                .contribution(teamMembers.getContribution())
                .build();
    }


}
