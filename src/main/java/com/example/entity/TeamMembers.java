package com.example.entity;

import com.example.dto.TeamMembersForm;
import com.example.dto.TeamsForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name = "Team_members")
public class TeamMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_member_id")
    private Long teamMemberId;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contribution")
    private Long contribution;


    public void create(Long teamId, Long userId, long l) {
        this.teamId = teamId;
        this.userId = userId;
        this.contribution = l;
    }

    public TeamMembersForm toDto(TeamMembers entity) {
        TeamMembersForm dto = new TeamMembersForm();
        dto.setTeamMemberId(entity.getTeamMemberId());
        dto.setTeamId(entity.getTeamId());
        dto.setUserId(entity.getUserId());
        dto.setContribution(entity.getContribution());
        return dto;
    }
}
