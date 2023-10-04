package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.dto.response.FeedbackStatusFeedbackYnUserIdResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatuses, Long> {






    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards.id = :boardId")
    List<FeedbackStatuses> findByBoardsId(@Param("boardId") Long boardId);

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards.id = :boardId AND fs.userId = :memberId")
    FeedbackStatuses findByBoardsIdAndUsersId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.userId = :memberId AND fs.teamId = :teamId")
    List<FeedbackStatuses> findFeedbackStatusesByMemberIdAndTeamId(@Param("memberId") Long memberId, @Param("teamId") Long teamId);
    // FeedbackStatuses를 일괄 저장하는 쿼리

    @Query("SELECT fs FROM FeedbackStatuses fs WHERE fs.boards = :boards")
    List<FeedbackStatuses> findByBoards(@Param("boards") Boards boards);

    @Query("SELECT NEW com.example.demo.src.file.dto.response.FeedbackStatusFeedbackYnUserIdResponse(fs.feedbackYn, fs.userId) FROM FeedbackStatuses fs WHERE fs.boards.id = :boardId")
    List<FeedbackStatusFeedbackYnUserIdResponse> findFeedbackYnAndUserIdByBoardsId(@Param("boardId") Long boardId);
}
