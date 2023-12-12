package com.example.demo.src.file.Service;


import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.Repository.FileRepository;
import com.example.demo.src.file.client.*;
import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Files;
import com.example.demo.src.file.dto.request.BoardWriteRequest;
import com.example.demo.src.file.dto.response.BoardDetailResponse;
import com.example.demo.src.file.dto.response.BoardResponse;
import com.example.demo.src.file.dto.response.PostsResponse;
import com.example.demo.src.file.dto.response.multiWriteResponse;

import com.example.demo.src.file.vo.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BoardService {



    private BoardRepository boardRepository;
    private FeedbackStatusRepository feedbackStatusRepository;
    private FileRepository fileRepository;
    private FileService fileService;
    private AlarmRepository alarmRepository;
    @Autowired
    WorkerServiceClient workerServiceClient;


    @Autowired
    TeamServiceClient teamServiceClient;

    @Autowired
    MemberServiceClient memberServiceClient;

    //조회수 증가
    public void increaseCount(Long boardId) {
        boardRepository.increaseViewCount(boardId);
    }

    public static Boards toEntity(BoardWriteRequest boardWriteRequest) {
        return Boards.builder()
                .title(boardWriteRequest.getTitle())
                .content(boardWriteRequest.getContent())
                .build();
    }

    //글 작성
    @Transactional(rollbackFor = Exception.class) // Exception이 발생하면 롤백
    public multiWriteResponse multiWrite(BoardWriteRequest request, Long memberId, Long teamId, Long workId, MultipartFile[] files) throws Exception {
        //memberId와 workId로 worker조회하기. 글을 쓰는 사람이 work를 담당한 worker인지 확인하기 위함

        //feign client 사용
        WorkerResponse workerResponse=workerServiceClient.getWriteStatus(memberId,workId);
        String projectPath = "/src/main/resources/static/files";

        //work의 담당자만 게시판을 작성할 수 있음
        //각각의 담당자마다 게시판을 한번만 작성할 수 있음
        if (workerResponse.isWriteYn()) {
            throw new IllegalStateException("Worker has already written a board.");
        }

        //woker가 게시판을 작성했음을 등록
        //feign client 사용
        workerServiceClient.setWriteStatusTrue(memberId,workId);
        // 게시판 등록
        //피드백을 등록 하기만 하면
        //work 상태를 피드백진행중=3으로 바꿈
        //한번 피드백을 했으면 다시 못바꿈)
        //workResponse.setStatus(3);
        workerServiceClient.setWorkStatus(workId, 3);

        Boards boards = toEntity(request);
        boards.setUserId(memberId);
        boards.setTeamId(teamId);
        boards.setWorkId(workId);

        boardRepository.save(boards);

        // Save all uploaded files
        if (files != null) {
            for (MultipartFile file : files) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + file.getOriginalFilename();
                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);


                // 빌더를 사용하여 파일 객체 생성
                Files file1 = Files.builder()
                        .filename(fileName)
                        .filepath("/files/" + fileName)
                        .build();
                file1.confirmBoard(boards);
                fileRepository.save(file1);

            }
        }


        // 멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장

        FeedbackStatusAndAlarm(boards,memberId,workId,teamId);
        return multiWriteResponse.from( boards);

    }


    //___________________________________________________________
    //일단 수정 요청을 하지 않아도, 팀원 모두에게 수정했다고 알람이 가도록 구현
    //추후에 수정요청을 한 사람에게만 알람이 가도록, 그리고 수정요청을 받지 않더라도 글 작성자가
    //게시글을 수정하고 싶을 경우에도 생각해야함.
    //다중 파일 글 재작성
    // 게시글 리스트 처리


    public List<BoardResponse> boardList(Long memberId, Long teamId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴
        // memberId와 teamId에 해당하는 게시글 리스트 조회
        // teamId로 해당 팀의 게시글 리스트 조회
        List<Boards> boardsList = boardRepository.findBoardsByTeamId(teamId);
        List<FeedbackStatuses> feedbackStatusesList=feedbackStatusRepository.findFeedbackStatusesByMemberIdAndTeamId(memberId,teamId);

        System.out.println("feedbackStatusesList.size()"+feedbackStatusesList.size());
        System.out.println("boardsList.size()"+boardsList.size());
        List<BoardResponse> boardResponses = new ArrayList<>();

        for (int i = 0; i < boardsList.size(); i++) {
            Boards board = boardsList.get(i);
            FeedbackStatuses feedbackStatus=feedbackStatusesList.get(i);
            BoardResponse boardResponse = BoardResponse.from(board);
            boardResponse.setFeedbackYn(feedbackStatus.getFeedbackYn());
            boardResponses.add(boardResponse);
        }
        return boardResponses;
    }


    public List<BoardResponse> professorBoardList( Long teamId) {
        // memberId를 사용하여 해당 멤버의 알람 리스트를 가져옴
        // memberId와 teamId에 해당하는 게시글 리스트 조회
        // teamId로 해당 팀의 게시글 리스트 조회
        List<Boards> boardsList = boardRepository.findBoardsByTeamId(teamId);



        System.out.println("boardsList.size()"+boardsList.size());
        List<BoardResponse> boardResponses = new ArrayList<>();

        for (int i = 0; i < boardsList.size(); i++) {
            Boards board = boardsList.get(i);
            BoardResponse boardResponse = BoardResponse.from(board);
            boardResponses.add(boardResponse);
        }
        return boardResponses;
    }

    public List<BoardWorkDto> workList(Long teamId) {
        return workerServiceClient.findWorksByTeamId(teamId);
    }

    public List<ResponseTeamMember> teamMemberList(Long teamId) {
        return teamServiceClient.findTeamById(teamId);
    }

    //특정 게시글 불러오기
    public BoardDetailResponse boardView(Long id){
        Boards boards = boardRepository.findBoardById(id);
        WorkResponse workResponse=workerServiceClient.findWorkById(boards.getWorkId());
        ResponseTeamMember memberDto=memberServiceClient.findByUserId(boards.getUserId());
        return BoardDetailResponse.from(boards,memberDto,workResponse);
    }

    //___________________________________________________________
    //일단 수정 요청을 하지 않아도, 팀원 모두에게 수정했다고 알람이 가도록 구현
    //추후에 수정요청을 한 사람에게만 알람이 가도록, 그리고 수정요청을 받지 않더라도 글 작성자가
    //게시글을 수정하고 싶을 경우에도 생각해야함.
    //다중 파일 글 재작성
    public multiWriteResponse multiReWrite(Long boardId,Long workId,BoardWriteRequest request, MultipartFile[] files) throws Exception{
        String projectPath= "/src/main/resources/static/files";

        Boards boards = boardRepository.findBoardById(boardId);

        boards.setWorkId(workId);

        boards.setTitle(request.getTitle());
        boards.setContent(request.getContent());


        // 새로 업로드 될 파일
        if (files != null) {
            for (MultipartFile file : files) {
                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + file.getOriginalFilename();
                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);


                // 빌더를 사용하여 파일 객체 생성
                Files file1 = Files.builder()
                        .filename(fileName)

                        .filepath("/files/" + fileName)
                        .build();
                file1.confirmBoard(boards);
                fileRepository.save(file1);

            }
        }

        boardRepository.save(boards);

        //if(mod_compl==true)
        //글작성자 제외 팀원 모두에게 알람이 가도록
        reWrtieCompletionAlarm(boards);
        return multiWriteResponse.from(boards);
    }

    public void reWrtieCompletionAlarm(Boards boards){
        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());
        List<FeedbackStatuses> feedbackStatusesList=feedbackStatusRepository.findByBoardsId(boards.getId());
        ResponseTeamMember writers=memberServiceClient.findByUserId(boards.getUserId());

        WorkResponse workResponse=workerServiceClient.findWorkById(boards.getWorkId());
        for (int i = 0; i < allMembers.size(); i++) {
            ResponseTeamMember memberResponse=allMembers.get(i);
            FeedbackStatuses feedbackStatuses=feedbackStatusesList.get(i);
            if (memberResponse == null || memberResponse.getId().equals(boards.getUserId())) {continue;}

            //만약 글 작성자 본인이라면 피드백 승인,거부를 할 필요가 없으므로 feedbackStatus 등록 필요 없음.
            //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감


            String userName = writers.getName(); //글 작성자 이름
            Integer studentNumber = writers.getStudentNumber(); //학번
            String workName =workResponse.getWorkName();
            String title = boards.getTitle();
            String message="";

            System.out.println("userName"+writers );
            //거절한 사람에게 가는 알림 메시지
            if(feedbackStatuses.getFeedbackYn()==2){
                message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "' 피드백을 반영하여 수정하였습니다.";
            }
            //피드백을 한적없거나 동의한 사람에게 가는 알람 메시지
            else if(feedbackStatuses.getFeedbackYn()==1||feedbackStatuses.getFeedbackYn()==0) {
                //알람 메시지 등록
                message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "' 수정하였습니다.";

            }

            String url = "/board/view/" + boards.getId();
            Alarms alarms = new Alarms();
            alarms.setUserId(memberResponse.getId()); // 연관관계 설정
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setAlarmKind("complUpdate");
            alarms.setBoardId(boards.getId());
            alarms.setWriterId(memberResponse.getId());
            alarmRepository.save(alarms);
        }
    }

    //wiriter은 게시판 작성자
    @Transactional
    public void FeedbackStatusAndAlarm(Boards boards, Long writerId, Long workId, Long teamId) {
        ResponseTeamMember selectedMember = null;
        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());
        ResponseTeamMember writers=memberServiceClient.findByUserId(writerId);
        WorkResponse works=workerServiceClient.findWorkById(workId);
        // FeedbackStatuses 및 Alarms 생성 및 설정을 수행하고 컬렉션에 추가
        List<FeedbackStatuses> feedbackStatusesList = allMembers.stream().map(member -> {
            FeedbackStatuses feedbackStatuses = new FeedbackStatuses();
            feedbackStatuses.confirmBoard(boards);
            feedbackStatuses.setUserId(member.getId());
            feedbackStatuses.setTeamId(teamId);
            feedbackStatuses.setFeedbackYn(member.getId().equals(writerId) ? 3 : 0);
            return feedbackStatuses;
        }).collect(Collectors.toList());

        // FeedbackStatuses를 일괄 저장
        feedbackStatusRepository.saveAll(feedbackStatusesList);

        // 알람 생성 및 설정을 수행하고 컬렉션에 추가

        // Alarms를 일괄 저장
        List<Alarms> alarmsList = allMembers.stream()
                .filter(member -> !member.getId().equals(writerId))
                .map(member -> {

                    String userName = writers.getName();
                    Integer studentNumber = writers.getStudentNumber();
                    String workName = works.getWorkName();
                    String title = boards.getTitle();
                    String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'에 대해 새로운 글을 등록 하였습니다.";
                    String url = "/board/view/" + boards.getId();

                    Alarms alarms = new Alarms();
                    alarms.setWriterPictureUrl(writers.getPictureUrl());
                    alarms.setUserId(member.getId());
                    alarms.setContent(message);
                    alarms.setRedirectUrl(url);
                    alarms.setAlarmKind("newWrite");
                    alarms.setBoardId(boards.getId());
                    alarms.setWriterId(member.getId());
                    return alarms;
                }).collect(Collectors.toList());

        // Alarms를 일괄 저장
        alarmRepository.saveAll(alarmsList);

    }

    //특정 게시글 삭제
    public void boardDelete(Long id){
        Boards boards = boardRepository.findBoardById(id);
        fileService.deletePhotoFromFileSystem(boards.getFileList());
        boardRepository.deleteById(id);
    }

    public List<PostsResponse> getPosts(Long workId){
        List<Boards> boards = boardRepository.findByWorksId(workId);
        List<PostsResponse> posts = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        boards.forEach(v -> {
            System.out.println(v);
            posts.add(modelMapper.map(v, PostsResponse.class));
        });
        return posts;
    }


}
