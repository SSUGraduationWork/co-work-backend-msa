package com.example.repository;

import com.example.entity.TeamMembers;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamMembersRepository extends CrudRepository<TeamMembers, Long> {

     List<TeamMembers> findByUserId(Long id);

     TeamMembers findByTeamIdAndUserId(Long teamId, Long userId);
}
