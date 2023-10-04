package com.example.repository;

import com.example.entity.TeamMembers;
import com.example.vo.Contribution;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMembersRepository extends CrudRepository<TeamMembers, Long> {

     List<TeamMembers> findByUserId(Long id);

     TeamMembers findByTeamIdAndUserId(Long teamId, Long userId);

     @Query("SELECT TM.userId FROM TeamMembers TM WHERE TM.teamId = :teamId")
     List<Long> findUserIdByTeamId(Long teamId);

     @Query("SELECT NEW com.example.vo.Contribution(TM.userId, TM.contribution) FROM TeamMembers TM WHERE TM.teamId = :teamId")
     List<Contribution> findContributionByTeamId(Long teamId);
}
