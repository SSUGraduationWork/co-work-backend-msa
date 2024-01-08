package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.dto.response.OneBoardIdResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Boards,Long>
{

    @Modifying
    @Query("UPDATE Boards b SET b.viewCnt = b.viewCnt + 1 WHERE b.id = :boardId")
    void increaseViewCount(@Param("boardId") Long boardId);

    @Query("SELECT b FROM Boards b WHERE b.id = :id")
    Boards findBoardById(@Param("id") Long id);

    @Query("SELECT b FROM Boards b WHERE b.workId = :workId")
    List<Boards> findByWorksId(Long workId);


    @Query("SELECT b FROM Boards b WHERE b.teamId = :teamId")
    List<Boards> findBoardsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT NEW com.example.demo.src.file.dto.response.OneBoardIdResponse(b.id,b.feedbackYn) FROM Boards b WHERE b.userId IN :userIdList AND b.workId = :workId")
    List<OneBoardIdResponse> findByUserIdInAndWorkId(@Param("userIdList") List<Long> userIdList, @Param("workId") Long workId);

}
