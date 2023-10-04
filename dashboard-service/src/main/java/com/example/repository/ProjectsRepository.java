package com.example.repository;

import com.example.entity.Projects;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProjectsRepository extends CrudRepository<Projects, Long> {

    ArrayList<Projects> findAll();

    ArrayList<Projects> findByProfessorId(Long professorId);

    Projects findByProjectName(String projectName);
    ArrayList<Projects> findByProfessorIdAndSemester(Long professrId, String semester);
    Projects findByProfessorIdAndProjectId(Long professorId, Long id);

//    @Query(SELECT * from (Members2 M inner join co_work.Projects2 P on M.id = P.professor_id)
//            INNER JOIN Teams2 T on P.professor_id = T.project_id)

}
