package com.example.controller;


import com.example.dto.UserDto;
import com.example.service.TeamMemberService;
import com.example.vo.TeamMemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
//@CrossOrigin(origins="http://localhost:8081")
@RequestMapping("/team-service")
public class TeamController {


    private final TeamMemberService teamMemberService;
    //멤버별로 알람 리스트 확인하기

    @GetMapping("/find/member/{teamId}")
    public List<UserDto> findTeamById(@PathVariable Long teamId) {
        return teamMemberService.findTeamById(teamId);
    }
    @GetMapping("/find/teammember/{teamId}/{memberId}")
    public ResponseEntity<TeamMemberResponse> findByTeamsIdAndUsersId(@PathVariable Long teamId, @PathVariable Long memberId) {
        return ResponseEntity.ok( teamMemberService.findByTeamsIdAndUsersId(teamId,memberId));
    }

    @PostMapping("/add/contribution")
    public void addContribution(@RequestBody  TeamMemberResponse teamMemberResponse){
        teamMemberService.addContribution(teamMemberResponse);
    }
}


