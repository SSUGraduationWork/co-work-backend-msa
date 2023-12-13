package com.example.controller;



import com.example.Exception.AlreadyExistException;
import com.example.Exception.ProjectNotFoundException;
import com.example.Exception.TeamNotFoundException;
import com.example.dto.*;
import com.example.entity.Projects;
import com.example.entity.Teams;
import com.example.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j //log
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    private class ResponseData {
        private int status;
        private String message;
        private Object data;

        public ResponseData(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    //1. project 생성
    @PostMapping("/dashboard/projects/{professorId}")
    public ResponseEntity<ResponseData> createProjects(@PathVariable Long professorId, @RequestBody RequestProjectsForm form) {

        //1. 동일한 projectName 존재 확인
        boolean check = dashboardService.checkProjectName(form.getProjectName());

        //2.동일한 projectName이 존재하지 않을 경우, project 생성
        if (check == false) {
            ProjectsForm projects = dashboardService.createProjects(professorId, form);
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        //3.Projects DB에 해당 projectName 존재할 경우, 기존의 Projects2 보여줌
        else {
            Projects original = dashboardService.getExistingProjects(form.getProjectName());
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", original);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
    }

    //2. project 조회: 교수별로 => Dashboard:교수 기본 화면
    @GetMapping("/dashboard/professor/{professorId}")
    public ResponseEntity<ResponseData> watchProjects(@PathVariable Long professorId) {
        List<Projects> projectsList = dashboardService.watchProjects(professorId);
        if (projectsList.isEmpty()) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projectsList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //3. project 수정
    @PatchMapping("/dashboard/projects/{projectId}")
    public ResponseEntity<ResponseData> editProject(@PathVariable Long projectId, @RequestBody RequestProjectsForm form) {
        //입력에 원하는 정보가 들어왔는지 확인
        ProjectsForm projects = dashboardService.editProjects(projectId, form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projects);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //4. project 삭제
    @DeleteMapping("/dashboard/projects/{projectId}")
    public ResponseEntity<ResponseData> deleteProject(@PathVariable Long projectId) {
        dashboardService.deleteProjects(projectId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //semester에 따라 대시보드에 보이는 프로젝트 다름
    @GetMapping("/dashboard/projects/{professorId}/{semester}")
    public ResponseEntity<ResponseData> watchProjectsBySemester(@PathVariable Long professorId, @PathVariable String semester) {
        List<Projects> projectsList = dashboardService.watchProjectsBySemester(professorId, semester);

        if (projectsList.isEmpty()) {
            return null;
        }

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", projectsList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


    //5. team 생성 => 학생. Dashboard에서 생성할 것
    @PostMapping("/dashboard/teams/{projectId}/{studentId}")
    public ResponseEntity<ResponseData> createTeams(@PathVariable Long projectId, @PathVariable Long studentId, @RequestBody RequestTeamsForm form) throws AlreadyExistException {

        // 해당 projectId가 project table에 존재하는지 확인. 없으면 오류 발생 => ProjectNotFoundException
        boolean check = dashboardService.checkProjectId(projectId); //check==true일 경우 DB에 해당 date 존재
        if (check == false) {
            throw new ProjectNotFoundException("Project Not Found");
        }

        // teamRepository에서 teamName이 겹치지 않는지 확인
        boolean checkTeamName = dashboardService.checkTeamName(form.getTeamName());

        if (checkTeamName == true) {
            throw new AlreadyExistException("Team Already Exist");
        }

        //1. 팀생성 + 팀 number 1
        TeamsForm teams = dashboardService.createTeams(projectId, form);

        //2. 팀멤버 테이블 추가
        dashboardService.createTeamsMembers(teams.getTeamId(), studentId);

        //3. 프로젝트 count 1씩
        dashboardService.countProjectNums(projectId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", teams);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //5. 1) 학생이 보낸 URL(team으로 직접 들어올 경우) => team_number 1 증가 + project_number 1 증가
    @PatchMapping("/dashboard/{projectId}/{teamId}/{userId}")
    public ResponseEntity<ResponseData> countNumbers(@PathVariable Long projectId, @PathVariable Long teamId, @PathVariable Long userId) {
        // 해당 projectId가 project table에 존재하는지 확인. 없으면 오류 발생 => ProjectNotFoundException
        boolean checkProject = dashboardService.checkProjectId(projectId); //check==true일 경우 DB에 해당 date 존재
        if (checkProject == false) {
            throw new ProjectNotFoundException("Project Not Found");
        }

        // 해당 team이 존재하는지
        boolean checkTeam = dashboardService.checkTeam(teamId);
        if (checkTeam == false) {
            throw new TeamNotFoundException("Team Not Found");
        }

        // 이미 멤버 테이블에 존재하는지 확인
        boolean checkMember = dashboardService.checkTeamMember(teamId, userId);
        if (checkMember == false) {     //팀멤버 테이블에 존재하지 않는경우
            //1. 팀멤버 테이블에 추가
            dashboardService.createTeamsMembers(teamId, userId);

            //2. teamnumber, projectnumber 1씩 증가
            dashboardService.countProjectNums(projectId);
            TeamsForm target = dashboardService.countTeamNums(teamId);

            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", target);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        else {
            ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", "Already exists");
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
    }


    //6. team 조회: project 별로 모든 팀 조회=> 교수, Dashboard에서 프로젝트 클릭시
    @GetMapping("/dashboard/teamsByPro/{projectId}")
    public ResponseEntity<ResponseData> watchTeamsByPro(@PathVariable Long projectId) {
        List<Teams> teamsList = dashboardService.watchTeamsByPro(projectId);

        String projectName = dashboardService.findProjectName(projectId);

        ResultForm resultForm = new ResultForm(teamsList, projectName, null);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", resultForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //7. team 조회: 학생Id 별로 해당하는 모든 팀 조회 => 학생 기본 화면
    @GetMapping("/dashboard/student/{id}")
    public ResponseEntity<ResponseData> watchTeamsByStu(@PathVariable Long id) {

        List<Teams> teamsList = dashboardService.watchTeamsByStu(id);

        List<TeamAndProjectForm> form = dashboardService.getProjectAndTeam(teamsList);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", form);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //8. semester에 따라 team 다르게 보이기
    @GetMapping("/dashboard/teams/{studentId}/{semester}")
    public ResponseEntity<ResponseData> watchTeamsBySemester(@PathVariable Long studentId, @PathVariable String semester) {
        List<Teams> teamsList = dashboardService.watchTeamsByStu(studentId);

        if (teamsList.isEmpty()) {
            return null;
        }

        List<Teams> resultTeamList = dashboardService.findTeamBySemester(teamsList, semester);
        List<Projects> projects2List = dashboardService.findProjectInfo(resultTeamList);
        ResultForm resultForm = new ResultForm(resultTeamList, null, projects2List);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", resultForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //9. team 수정
    @PostMapping("/dashboard/teams/{teamId}")
    public ResponseEntity<ResponseData> updateTeams(@PathVariable Long teamId, @RequestBody RequestTeamsForm form) {
        Teams teams = dashboardService.editTeams(teamId, form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", teams);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //10. teams 삭제
    @DeleteMapping("/dashboard/teams/{teamId}")
    public ResponseEntity<ResponseData> deleteTeams(@PathVariable Long teamId) {
        dashboardService.deleteTeams(teamId);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //11. 팀 멤버 정보 반환
    @GetMapping("/{teamId}/team-members")
    public ResponseEntity<TeamMembersInfo> getTeamMembers(@PathVariable("teamId") Long teamId){
        TeamMembersInfo teamMembers = dashboardService.getTeamMembers(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(teamMembers);
    }

    //12. team info by teamId
    @GetMapping("/team-info/{teamId}")
    public ResponseEntity<TeamInfoRes> getTeamInfo(@PathVariable("teamId") Long teamId){
        TeamInfoRes teamInfo = dashboardService.getTeamInfo(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(teamInfo);
    }

}
