package com.example.demo.src.file.dto.response;

import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.Files;

import com.example.demo.src.file.vo.ResponseTeamMember;
import com.example.demo.src.file.vo.WorkResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
public class BoardDetailResponse {

    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> fileDirs;
    private List<Long> fileId;
    private String workName;
    private Long writerId;
    //왜 지웠는지?
    private Long workId;

    private String writer;
    private Integer studentNumber;
    private String pictureURL;

//사용자명과 작업명은 추후 추가해야함
//private String username;

//private String workname;

    @Builder
    public BoardDetailResponse(Long boardId, String title,String content,LocalDateTime createdAt,List<String> fileDirs,List<Long> fileId,String workName, Long writerId,Long workId,String writer, Integer studentNumber, String pictureURL){
        this.boardId = boardId;
        this.title = title;
        this.content=content;
        this.createdAt=createdAt;
        this.fileDirs=fileDirs;
        this.fileId=fileId;
        this.workName=workName;
        this.writerId=writerId;
        this.workId=workId;
        this.writer = writer;
        this.pictureURL = pictureURL;
        this.studentNumber = studentNumber;

    }


    public static BoardDetailResponse from(Boards boards, ResponseTeamMember memberResponse, WorkResponse workResponse) {
        BoardDetailResponseBuilder responseBuilder = BoardDetailResponse.builder()
                .boardId(boards.getId())
                .title(boards.getTitle())
                .content(boards.getContent())
                .workName(workResponse.getWorkName())
                .writerId(memberResponse.getId())
                .workId(boards.getWorkId())
                .createdAt(boards.getCreatedAt())
                .workName(workResponse.getWorkName())
                .writer(memberResponse.getName())
                .pictureURL(memberResponse.getPictureUrl())
                .studentNumber(memberResponse.getStudentNumber());
        List<String> fileDirs = new ArrayList<>();
        for (Files file : boards.getFileList()) {
            fileDirs.add(file.getFilepath());
        }

        List<Long> fileId = new ArrayList<>();
        for (Files file : boards.getFileList()) {
            fileId.add(file.getId());
        }

        responseBuilder.fileDirs(fileDirs);
        responseBuilder.fileId(fileId);
        return responseBuilder.build();
    }


}