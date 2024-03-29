package com.example.repository;

import com.example.entity.Teams;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Repository
public interface TeamsRepository extends CrudRepository<Teams, Long> {
    ArrayList<Teams> findAll();

    ArrayList<Teams> findByProjectId(Long projectId);
    Teams findByTeamIdAndProjectId(Long teamId, Long projectId);

    Teams findByTeamName(String teamName);

    Teams findByTeamId(Long teamId);

}
