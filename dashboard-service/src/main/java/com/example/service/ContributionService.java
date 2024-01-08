package com.example.service;


import com.example.client.UserServiceClient;
import com.example.client.WorkServiceClient;
import com.example.dto.ContributionRes;
import com.example.repository.TeamMembersRepository;
import com.example.vo.Contribution;
import com.example.vo.UserContribution;
import com.example.vo.WorkProgress;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContributionService {
    private final TeamMembersRepository teamMembersRepository;
    private final UserServiceClient userServiceClient;
    private final WorkServiceClient workServiceClient;

    public ContributionRes getContributionInfo(Long teamId){
        List<Contribution> contributions = teamMembersRepository.findContributionByTeamId(teamId);

        List<UserContribution> teamMembers = new ArrayList<>();
        WorkProgress progress = null;

        try{
            teamMembers = userServiceClient.getUsersContribution(contributions);
            progress = workServiceClient.getWorkProgress(teamId);

        } catch(FeignException ex){
            log.error(ex.getMessage());
        }
        return new ContributionRes(progress, teamMembers);
    }

}
