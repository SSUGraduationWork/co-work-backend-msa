package com.example.service;

import com.example.Exception.ProjectNotFoundException;
import com.example.client.UserServiceClient;
import com.example.dto.*;
import com.example.entity.Projects;
import com.example.entity.TeamMembers;
import com.example.entity.Teams;
import com.example.repository.ProjectsRepository;
import com.example.repository.TeamMembersRepository;
import com.example.repository.TeamsRepository;
import com.example.vo.UserIdList;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {
    private final UserServiceClient userServiceClient;
    private final ProjectsRepository projectsRepository;
    private final TeamsRepository teamsRepository;

    private final TeamMembersRepository teamMembersRepository;

    //1. 해당 projectName이 존재하는지 확인
    @Transactional
    public boolean checkProjectName(String projectName) {
        boolean check = false;
        if (projectsRepository.findByProjectName(projectName) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public Projects getExistingProjects(String projectName) {
        Projects original = projectsRepository.findByProjectName(projectName);
        return original;
    }

    @Transactional
    public ProjectsForm createProjects(Long professorId, RequestProjectsForm form) {
        Projects projects = new Projects();
        projects.create(professorId, form.getProjectName(), form.getSemester(), 1L);
        projectsRepository.save(projects);
        ProjectsForm targetForm = projects.toDto(projects);

        return targetForm;
    }

    @Transactional
    public List<Projects> watchProjects(Long professor_id) {
        List<Projects> projectsList = projectsRepository.findByProfessorId(professor_id);
        return projectsList;
    }

    @Transactional
    public ProjectsForm editProjects(Long projectId, RequestProjectsForm dto) {
        Projects project = projectsRepository.findById(projectId).orElse(null);
        System.out.println("project: "+project);
        if (project != null) {
            project.update(dto.getProjectName(), dto.getSemester());
        } else {
            throw new ProjectNotFoundException("Project Not Found");
        }
        ProjectsForm targetForm = project.toDto(project);
        return targetForm;
    }

    @Transactional
    public void deleteProjects(Long projectId) {
        Projects project = projectsRepository.findById(projectId).orElse(null);

        projectsRepository.delete(project);
    }

    @Transactional
    public List<Projects> watchProjectsBySemester(Long professor_id, String semester) {
        List<Projects> projectsList = projectsRepository.findByProfessorIdAndSemester(professor_id, semester);

        return projectsList;
    }

    //2-3. 해당 project가 존재하는지: projectId로
    @Transactional
    public boolean checkProjectId(Long projectId) {
        boolean check = false;
        if (projectsRepository.findById(projectId) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public boolean checkTeamName(String teamName) {
        boolean check = false;
        if (teamsRepository.findByTeamName(teamName) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public TeamsForm createTeams(Long projectId, RequestTeamsForm dto) {
        Teams teams = new Teams();
        teams.create(projectId, dto.getTeamName(), 1L);
        teamsRepository.save(teams);
        TeamsForm targetDto = teams.toDto(teams);
        return targetDto;
    }


    //팀멤버테이블에 추가 -> 미완성
    @Transactional
    public void createTeamsMembers(Long teamId, Long userId) {
        TeamMembers teamMembers = new TeamMembers();
        teamMembers.create(teamId, userId, 0L);

        teamMembersRepository.save(teamMembers);
    }

    @Transactional
    public void countProjectNums(Long projectId) {
        Projects projects = projectsRepository.findById(projectId).orElse(null);
        projects.updateProjectNumber(projects.getProjectNumber() + 1);
    }

    @Transactional
    public TeamsForm countTeamNums(Long teamId) {
        Teams teams = teamsRepository.findByTeamId(teamId);
        teams.updateTeamNumber(teams.getTeamNumber() + 1);

        TeamsForm form = teams.toDto(teams);
        return form;
    }

    //2-5. team에 해당 사용자가 존재하는지
    @Transactional
    public boolean checkTeamMember(Long teamId, Long userId) {
        boolean check = false;
        if (teamMembersRepository.findByTeamIdAndUserId(teamId, userId) != null) {
            check = true;
        }
        return check;
    }

    //2-4. projectId와 일치하는 team이 존재하는지
    @Transactional
    public boolean checkTeam(Long teamId) {
        boolean check = false;
        if (teamsRepository.findById(teamId) != null) {
            check = true;
        }
        return check;
    }

    @Transactional
    public List<Teams> watchTeamsByPro(Long projectId) {
        List<Teams> teamsList = teamsRepository.findByProjectId(projectId);
        return teamsList;
    }

    @Transactional
    public String findProjectName(Long projectId) {
        Projects projects = projectsRepository.findById(projectId).orElse(null);
        String projectName = projects.getProjectName();

        return projectName;
    }

    @Transactional
    public List<Teams> watchTeamsByStu(Long id) {
        List<Long> teamsId = new ArrayList<>();
        List<Teams> teams2List = new ArrayList<>();

        List<TeamMembers> teamMembersList = teamMembersRepository.findByUserId(id);

        int cnt = teamMembersList.size();

        for (int i=0; i<cnt; i++) {
            TeamMembers teamMembers = teamMembersList.get(i); //객체 전체 선택
            teamsId.add(teamMembers.getTeamId());             //각 객체에서 teamId만 teamsId에 넣기
        }

        int count = teamsId.size();

        for (int j=0; j<count; j++) {
            Teams teams = teamsRepository.findById(teamsId.get(j)).orElse(null);
            teams2List.add(teams);
        }

        return teams2List;
    }

    @Transactional
    public List<TeamAndProjectForm> getProjectAndTeam(List<Teams> teamsList) {
        List<TeamAndProjectForm> result = new ArrayList<>();
        for (Teams teams:teamsList) {
            Projects projects = projectsRepository.findById(teams.getProjectId()).orElse(null);
            TeamAndProjectForm resultForm = new TeamAndProjectForm(teams.getTeamId(), teams.getProjectId(), teams.getTeamName(), teams.getTeamNumber(), projects.getSemester(), projects.getProjectName());
            result.add(resultForm);
        }

        return result;
    }

    @Transactional
    public List<Teams> findTeamBySemester(List<Teams> teamsList, String semester) {
        List<Teams> resultTeamList = new ArrayList<>();
        Long projectId = null;

        for (Teams teams:teamsList) {
            projectId = teams.getProjectId();
            log.info("projectId: "+ teams.toString());
            log.info("project"+ teams.getProjectId().toString());
            Projects projects = projectsRepository.findById(projectId).orElse(null);
            log.info("projects2: "+projects.toString());
            log.info("projects2.getSemester: "+ projects.getSemester().toString()+ "semester: "+ semester.toString());
            if (projects.getSemester().equals(semester)) {
                resultTeamList.add(teams);
            }
        }
        log.info("resultTeamList: "+resultTeamList.toString());

        return resultTeamList;
    }

    @Transactional
    public List<Projects> findProjectInfo(List<Teams> teamsList) {
        List<Projects> projects2List = new ArrayList<>();

        for (Teams teams: teamsList) {
            Long projectId = teams.getProjectId();
            projects2List.add(projectsRepository.findById(projectId).orElse(null));
        }

        return projects2List;
    }

    @Transactional
    public Teams editTeams(Long teamId, RequestTeamsForm dto) {
        Teams team = teamsRepository.findByTeamId(teamId);

        if (dto.getTeamName() != null) {
            team.updateTeamName(dto.getTeamName());
        }

        return team;
    }

    @Transactional
    public void deleteTeams(Long teamId) {
        Teams team = teamsRepository.findByTeamId(teamId);

        teamsRepository.delete(team);
    }

    public TeamMembersInfo getTeamMembers(Long teamId){
        List<Long> teamMemberIds = teamMembersRepository.findUserIdByTeamId(teamId);

        try{
            List<UserDto> teamMembers = userServiceClient.getUsersInfo(new UserIdList(teamMemberIds));
            return new TeamMembersInfo(teamMembers);
        } catch(FeignException ex){
            log.error(ex.getMessage());
            return new TeamMembersInfo(new ArrayList<>());
        }
    }
}
