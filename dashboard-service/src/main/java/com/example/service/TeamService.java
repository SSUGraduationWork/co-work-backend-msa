package com.example.service;


import com.example.repository.TeamsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamService {
private TeamsRepository teamsRepository;

public  void findTeamById(Long teamId){
    teamsRepository.findByTeamId(teamId);
}


}
