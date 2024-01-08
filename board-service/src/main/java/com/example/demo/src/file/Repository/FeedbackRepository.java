package com.example.demo.src.file.Repository;


import com.example.demo.src.file.domain.Feedbacks;
import com.example.demo.src.file.dto.response.BoardFeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedbacks,Long>
{


    @Query("SELECT f FROM Feedbacks f WHERE f.boards.id = :boardId")
    List<Feedbacks> findByBoardsId(@Param("boardId") Long board);

    @Query("SELECT NEW com.example.demo.src.file.dto.response.BoardFeedbackResponse(f.id,f.comment,f.createdAt,f.writerId,f.modReq)  FROM Feedbacks f WHERE f.boards.id = :boardId")
    List<BoardFeedbackResponse> findFeedbackByBoardsId(@Param("boardId") Long board);


}