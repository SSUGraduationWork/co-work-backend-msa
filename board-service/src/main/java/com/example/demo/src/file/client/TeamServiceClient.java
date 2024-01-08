package com.example.demo.src.file.client;


import com.example.demo.src.file.vo.ResponseTeamMember;
import com.example.demo.src.file.vo.TeamMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="dashboard-service" )
public interface TeamServiceClient {

    @GetMapping("/teammember/members/{teamId}")
    List<ResponseTeamMember> findTeamById(@PathVariable Long teamId);

    @GetMapping("/teammember/member/{teamId}/{memberId}")
    TeamMemberResponse findByTeamsIdAndUsersId(@PathVariable Long teamId,@PathVariable Long memberId);

    @PostMapping("/teammember/add-contribution")
    void addContribution(@RequestBody TeamMemberResponse teamMemberResponse);
}
