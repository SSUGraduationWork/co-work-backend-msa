package com.example.apigateway.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "Boards")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;
    @Column(name = "team_id")
    private Long teamId;
    @Column(name = "work_id")
    private Long workId;
    @Column(name = "user_id")
    private Long userId;
    private String title;
    private String content;
    @Column(name="feedback_yn")
    private Integer feedbackYN;
    @Column(name="view_cnt")
    private Integer viewCnt;
    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
