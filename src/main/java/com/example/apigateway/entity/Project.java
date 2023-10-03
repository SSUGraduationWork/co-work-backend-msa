package com.example.apigateway.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Projects")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "professor_id")
    private Long professorId;
    @Column(name = "project_name")
    private String projectName;

    private String semester;
    @Column(name="project_number")
    private Integer projectNumber;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();
}
