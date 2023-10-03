package com.example.apigateway.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long teamId;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "team_name")
    private String teamName;
    @Column(name="team_number")
    private Integer teamNumber;
}
