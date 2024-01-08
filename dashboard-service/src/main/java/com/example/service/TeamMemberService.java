package com.example.service;


import com.example.client.UserServiceClient;
import com.example.client.WorkServiceClient;
import com.example.dto.UserDto;
import com.example.entity.TeamMembers;
import com.example.repository.TeamMembersRepository;
import com.example.vo.TeamMemberResponse;
import com.example.vo.UserIdList;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamMemberService {
    private TeamMembersRepository teamMembersRepository;

    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private WorkServiceClient workServiceClient;

    public List<UserDto> findTeamById(Long teamId){

        List<Long> results= teamMembersRepository.findUserIdByTeamId(teamId);
        UserIdList userIdList=new UserIdList();
        userIdList.setUserIdList(results);
        return  userServiceClient.getUsersInfo(userIdList);
    }
    public TeamMemberResponse findByTeamsIdAndUsersId(Long teamId, Long memberId){
        TeamMembers teamMembers= teamMembersRepository.findByTeamIdAndUserId(teamId,memberId);
        return TeamMemberResponse.from(teamMembers);
    }


    public void addContribution(TeamMemberResponse teamMemberResponse) {
            TeamMembers teamMembers= teamMembersRepository.findByTeamIdAndUserId(teamMemberResponse.getTeamId(),teamMemberResponse.getUserId());
            // AlarmDTO를 Alarm으로 변환하여 저장
            teamMembers.addContribution(teamMemberResponse.getContribution());
            teamMembersRepository.save(teamMembers);
    }


    }
