package com.example.entity;

import com.example.dto.ProjectsForm;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name="Projects")
public class Projects {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "professor_id")
    private Long professorId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "semester")
    private String semester;

    @Column(name = "project_number")
    private Long projectNumber;

    public void create(Long professorId, String projectName, String semester, Long projectNumber) {
        this.professorId = professorId;
        this.projectName = projectName;
        this.semester = semester;
        this.projectNumber = projectNumber;
    }

    public void update(String projectName, String semester) {
        this.projectName = projectName;
        this.semester = semester;
    }

    public void updateProjectNumber(Long projectNumber) {
        this.projectNumber = projectNumber;
    }

    public ProjectsForm toDto(Projects entity) {
        ProjectsForm dto = new ProjectsForm();
        dto.setProjectId(entity.getProjectId());
        dto.setProfessorId(entity.getProfessorId());
        dto.setProjectName(entity.getProjectName());
        dto.setSemester(entity.getSemester());
        dto.setProjectNumber(entity.getProjectNumber());
        return dto;
    }

}
