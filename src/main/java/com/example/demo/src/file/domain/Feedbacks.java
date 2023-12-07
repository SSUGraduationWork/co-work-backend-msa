package com.example.demo.src.file.domain;


import com.example.demo.src.file.FeedbackTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="Feedbacks")
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Feedbacks extends FeedbackTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Boards boards;



    @Column(name = "writer_id")
    private Long writerId;

    @Column(name="mod_req")
    private Integer modReq;

    @Column(length = 500)
    private String comment;




    //연관관계 편의 메소드
    public void confirmBoard(Boards boards){
        this.boards = boards;
        boards.addFeedbacks(this);
    }




    // comment 필드의 getter 메서드 정의
    public String getComment() {
        return comment;
    }

    @Builder
    public Feedbacks(Long id, Boards boards, String comment){
        this.id=id;
        this.boards = boards;
        this.comment=comment;
        this.modReq=0;
    }

    //게시판에 대한 피드백 승인 메소드
    public void feedbackAgree(){
        this.modReq = 1;
    }

    //게시판에 대한 피드백 거절 메소드
    public void feedbackDeny(){
        this.modReq = 2;
    }
}
