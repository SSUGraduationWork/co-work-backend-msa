package com.example.vo;

import lombok.Data;

@Data
public class WorkProgress {
    private Integer totalWorks;
    private Integer notStarted;
    private Integer inProgress;
    private Integer done;
}
