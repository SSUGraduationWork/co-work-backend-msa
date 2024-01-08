package com.example.demo.src.file.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Entity
@Table(name = "Feedback_statuses")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FeedbackStatuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_status_id")
    private Long Id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",  nullable = false)
    private Boards boards;

    @Column(name = "team_id")
    private Long teamId;

    @Column(name = "feedback_yn")
    private Integer feedbackYn;


    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards = boards;
        boards.addFeedbackStatuses(this);
    }


    //게시판에 대한 피드백 승인 메소드
    public void feedbackAgree(){
        this.feedbackYn = 1;
    }

    //게시판에 대한 피드백 거절 메소드
    public void feedbackDeny(){
        this.feedbackYn = 2;
    }



}