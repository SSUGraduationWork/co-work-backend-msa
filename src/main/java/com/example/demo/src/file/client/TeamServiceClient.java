package com.example.demo.src.file.client;

import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.vo.MemberResponse;
import com.example.demo.src.file.vo.TeamMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="team-service" )
public interface TeamServiceClient {

    @GetMapping("/team-service/find/member/{teamId}")
    List<MemberResponse> findTeamById(@PathVariable Long teamId);

    @GetMapping("/team-service/find/teammember/{teamId}/{memberId}")
    TeamMemberResponse findByTeamsIdAndUsersId(@PathVariable Long teamId,@PathVariable Long memberId);

    @PostMapping("/team-service/add/contribution")
    void addContribution(@RequestBody TeamMemberResponse teamMemberResponse);
}
