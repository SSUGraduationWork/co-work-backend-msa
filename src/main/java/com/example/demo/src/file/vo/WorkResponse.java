package com.example.demo.src.file.vo;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkResponse {

    private Long id;
    private Long teamId;
    private String workName;
    private Integer importance;
    private Integer status;
    private Integer workerNumber;
    private LocalDateTime endDate;

    public WorkResponse() {
        // 기본 생성자 내용 (선택적)
    }

    @Builder
    public WorkResponse(Long workId, String workName){
        this.id=workId;
        this.workName=workName;
    }


}
