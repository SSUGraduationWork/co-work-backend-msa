package com.example.demo.src.file.vo;

import com.example.demo.src.file.domain.Boards;
import lombok.Data;

@Data
public class FeedbackResponse {

    private Long id;

    private Boards boards;

    private Long writers;

    private boolean modReq;

    private String comment;

    private boolean agree;

}
