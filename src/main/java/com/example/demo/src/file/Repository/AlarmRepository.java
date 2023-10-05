package com.example.demo.src.file.Repository;

import com.example.demo.src.file.domain.Alarms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AlarmRepository extends JpaRepository<Alarms, Long> {
    @Query("SELECT a, f.feedbackYn " +
            "FROM Alarms a " +
            "LEFT JOIN FeedbackStatuses f " +
            "ON a.boardId = f.boards.id AND f.userId = :memberId " +
            "WHERE a.userId = :memberId")
    List<Object[]> findAlarmsWithFeedbackYn(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE Alarms a SET a.seen = true WHERE a.alarmId = :alarmId")
    void updateSeenStatus(@Param("alarmId") Long alarmId);
}

