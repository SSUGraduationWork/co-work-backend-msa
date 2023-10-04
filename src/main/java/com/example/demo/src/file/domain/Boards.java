package com.example.demo.src.file.domain;

import com.example.demo.src.file.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Boards")
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Boards extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "team_id")
    private Long teamId;


    @Column(name = "work_id")
    private Long workId;


    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 3000)
    private String content;


    @Column(name = "view_cnt")
    private Long viewCnt;     //조회수

    @Column(name = "feedback_yn")
    private boolean feedbackYn;

    //== 게시글을 삭제하면 달려있는 댓글 모두 삭제 ==//
    @JsonIgnore
    @OneToMany(mappedBy = "boards", cascade = ALL, orphanRemoval = true)
    private List<Feedbacks> feedbacksList = new ArrayList<>();

    //== 게시글을 삭제하면 달려있는 댓글 모두 삭제 ==//
    @JsonIgnore
    @OneToMany(mappedBy = "boards", cascade = ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FeedbackStatuses> feedbackStatusList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "boards", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> fileList = new ArrayList<>();


    public Boards updateViewCount(Long viewCount){
        this.viewCnt = viewCount+1;
        return this;
    }

    //피드백 추가,연관관계 편의 메소드
    public void addFeedbacks(Feedbacks feedbacks){
        //comment의 Post 설정은 comment에서 함
        feedbacksList.add(feedbacks);
    }

    //파일 추가,연관관계 편의 메소드
    public void addFiles(Files files){
        //comment의 Post 설정은 comment에서 함
        fileList.add(files);
    }

    public void addFeedbackStatuses(FeedbackStatuses feedbackStatuses){
        //comment의 Post 설정은 comment에서 함
        feedbackStatusList.add(feedbackStatuses);
    }


    @Builder
    public Boards(String title, String content){
        this.title = title;
        this.content = content;
        this.viewCnt = 0L;
        this.feedbackYn=false;
    }
}
