package com.example.demo.src.file.domain;

import com.example.demo.src.file.FeedbackTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Alarms")
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Alarms extends FeedbackTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long alarmId;


    @Column(name = "user_id")
    private Long userId;


    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "seen", nullable = false)
    private boolean seen;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "redirect_url", length = 100)
    private String redirectUrl;


    @Column(name = "writer_picture_url")
    private String writerPictureUrl;

    @Column(name = "alarm_kind")
    private String alarmKind;

    @Column(name = "writer_id")
    private Long writerId;

    //연관관계 편의 메소드



}