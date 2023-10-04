package com.example.vo;


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
    private boolean delYn;
    private LocalDateTime endDate;

    @Builder
    public WorkResponse(Long workId, String workName){
        this.id=workId;
        this.workName=workName;
    }


}
