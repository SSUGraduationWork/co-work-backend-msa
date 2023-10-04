package com.example.entity;

import com.example.dto.ProjectsForm;
import com.example.dto.TeamsForm;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Table(name="Teams")
public class Teams {
    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_number")
    private Long teamNumber;

    public void create(Long projectId, String teamName, Long teamNumber) {
        this.projectId = projectId;
        this.teamName = teamName;
        this.teamNumber = teamNumber;
    }

    public void updateTeamNumber(Long teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void updateTeamName(String teamName) {
        this.teamName = teamName;
    }

    public TeamsForm toDto(Teams entity) {
        TeamsForm dto = new TeamsForm();
        dto.setTeamId(entity.getTeamId());
        dto.setProjectId(entity.getProjectId());
        dto.setTeamName(entity.getTeamName());
        dto.setTeamNumber(entity.getTeamNumber());
        return dto;
    }

}
