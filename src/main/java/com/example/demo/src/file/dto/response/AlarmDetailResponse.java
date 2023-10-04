package com.example.demo.src.file.dto.response;


import com.example.demo.src.file.domain.Alarms;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmDetailResponse {
    private Long alarmId;

    private boolean seen;

    private String content;

    private String redirectUrl;

    private LocalDateTime createdTime;

    private String alarmKind;

    @Column(name = "picture_url", length = 100)
    private String pictureUrl;


    private Integer feedbackYn;//수정 요청 여부 0이면 수정 거부한거임-> 추후에 변경예정
    //0과1로 하면 생기는 문제점이 피드백을 하지 않은 사람도 거부한걸로 됨
    //string으로 바꾸어서 안한 상태, 거부, 승인 이렇게 3가지 경우로 바꿔야 함

    private  Long writerId;

    private Long boardId;

    @Builder
    public AlarmDetailResponse(Long alarmId, Long writerId, Long boardId, boolean seen, Integer feedbackYn, String content, String redirectUrl, LocalDateTime createdTime, String pictureUrl, String alarmKind){
        this.alarmId = alarmId;
        this.writerId=writerId;
        this.boardId=boardId;
        this.seen = seen;
        this.feedbackYn=feedbackYn;
        this.content=content;
        this.redirectUrl=redirectUrl;
        this.createdTime=createdTime;
        this.pictureUrl=pictureUrl;
        this.alarmKind=alarmKind;
    }

    public static AlarmDetailResponse from(Alarms alarms) {
        return AlarmDetailResponse.builder()
                .alarmId(alarms.getAlarmId())
                .boardId(alarms.getBoardId())
                .seen(alarms.isSeen())
                .content(alarms.getContent())
                .redirectUrl(alarms.getRedirectUrl())
                .createdTime(alarms.getCreatedAt())
                .pictureUrl(alarms.getWriterPictureUrl())
                .alarmKind(alarms.getAlarmKind())
                .writerId(alarms.getWriterId())
                .build();
    }


}



