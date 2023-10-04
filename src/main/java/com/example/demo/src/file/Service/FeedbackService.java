package com.example.demo.src.file.Service;


import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.FeedbackRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.client.*;
import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Feedbacks;
import com.example.demo.src.file.dto.request.FeedbackRequest;
import com.example.demo.src.file.dto.response.FeedbackResponse;
import com.example.demo.src.file.vo.AlarmDTO;
import com.example.demo.src.file.vo.MemberResponse;
import com.example.demo.src.file.vo.TeamMemberResponse;
import com.example.demo.src.file.vo.WorkResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class FeedbackService {


    private FeedbackRepository feedbackRepository;
    private BoardRepository boardRepository;
    private FeedbackStatusRepository feedbackStatusRepository;

    @Autowired
    AlarmServiceClient alarmServiceClient;
    @Autowired
    WorkerServiceClient workerServiceClient;

    @Autowired
    TeamServiceClient teamServiceClient;
    @Autowired
    MemberServiceClient memberServiceClient;


    public FeedbackResponse save(Long boardId, Long memberId, FeedbackRequest requset, Integer isApproved) {
        //피드백 등록
        Boards boards = boardRepository.findBoardById(boardId);
        MemberResponse writers = memberServiceClient.findByUserId(memberId);//피드백 작성자
        WorkResponse workResponse=workerServiceClient.findWorkById(boards.getWorkId());
        Feedbacks feedbacks = toEntity(requset);
        feedbacks.confirmBoard(boards);
        feedbacks.setWriterId(writers.getId());
        feedbackRepository.save(feedbacks);
        OneTeaamMemberAgreeCheck(isApproved, boards, writers,workResponse);

        // 팀원 모두 동의하면 boards의 feedback_yn=true로 변경
        AllTeamMemberAgreeCheck(boards,workResponse);

        return FeedbackResponse.from(feedbacks, boards,writers);
    }

    public void OneTeaamMemberAgreeCheck(Integer isApproved, Boards boards, MemberResponse writers,WorkResponse workResponse) {
        //-----------------------------
        FeedbackStatuses feedbackStatuses = feedbackStatusRepository.findByBoardsIdAndUsersId(boards.getId(), writers.getId());
        //한번도 피드백을 이미 한 경우 예외처리
        if (feedbackStatuses.getFeedbackYn() != 0) {
            return;
        }
        //피드백을 등록 하기만 하면
        //work 상태를 피드백진행중=3으로 바꿈

        //한번 피드백을 했으면 다시 못바꿈)


        workResponse.setStatus(3);
        workerServiceClient.updateWork(workResponse);

        // works.getEndDate()가 현재 시간보다 이후인지 확인합니다.
        //마감기한 전에 피드백 했을 때만 점수 받을 수 있음
        //writer에게 기여도 +1증가, 게시판 작성자는 피드백 점수에서 제외
        LocalDateTime currentTime = LocalDateTime.now();
        if (workResponse.getEndDate().isAfter(currentTime)) {
            TeamMemberResponse teamMembers = teamServiceClient.findByTeamsIdAndUsersId(boards.getTeamId(),writers.getId());
            teamMembers.setContribution(1);
            teamServiceClient.addContribution(teamMembers);
        }


        //피드백 작성자가 동의 하면 feedbackStauses의 feedback_yn=true로 변경
        //중복 코드 수정, feedbackStatus조회
        if (isApproved == 1) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatuses.feedbackAgree();
        } else if ((isApproved == 2)) {
            // 거부한 경우 feedbackYn=false로 바꾸기
            feedbackStatuses.feedbackDeny();
            //피드백에서 수정 요청 시  수정 요청한 본인 제외 모든 팀의 모든 팀원들에게 알람이 감
            FeedbackStatusAndAlarm(boards, writers,workResponse);
        }
        feedbackStatusRepository.save(feedbackStatuses);
    }

    //멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
    public void FeedbackStatusAndAlarm(Boards boards, MemberResponse writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //피드백에서 수정 요청 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getUserName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목


        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'에 대해 수정 요청을 하였습니다.";
        String url = "/board/view/" + boards.getId();
        List<MemberResponse> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        for (MemberResponse member : allMembers) {

            if (member.equals(writers)) {
                continue;
            }


            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setWriterPictureUrl(writers.getPictureUrl());
            alarmDTO.setUserId(member.getId());
            alarmDTO.setContent(message);
            alarmDTO.setRedirectUrl(url);
            alarmDTO.setAlarmKind("requestFeedback");
            alarmDTO.setBoardId(boards.getId());
            alarmDTO.setWriterId(writers.getId());
            alarmServiceClient.createAlarm(alarmDTO);
        }
    }


    //???? 완료 되었을 때도 알람이 가게 할지??
    public void AllTeamMemberAgreeCheck(Boards boards, WorkResponse workResponse) {

        // Board에 해당하는 모든 FeedbackStatuses 조회
        List<FeedbackStatuses> feedbackStatusesList = feedbackStatusRepository.findByBoards(boards);

        // FeedbackStatuses가 존재하는 경우에만 처리
        if (!feedbackStatusesList.isEmpty()) {
            boolean hasFeedbackYnTrue = true;

            // 모든 FeedbackStatuses의 feedback_yn이 true인지 확인
            for (FeedbackStatuses feedbackStatuses : feedbackStatusesList) {
                if (feedbackStatuses.getFeedbackYn() == 0 || feedbackStatuses.getFeedbackYn() == 2) { //한명이라도 feedback을 안했으면
                    hasFeedbackYnTrue = false;
                    break;
                }
            }

            // 모든 FeedbackStatuses의 feedback_yn이 true라면 board의 feedback_yn도 true로 변경
            if (hasFeedbackYnTrue && boards.isFeedbackYn() == false) {


                LocalDateTime currentTime = LocalDateTime.now();
                // works.getEndDate()가 현재 시간보다 이후인지 확인합니다.
                //마감기한 전에 피드백 했을 때만 점수 받을 수 있음
                if (workResponse.getEndDate().isAfter(currentTime)) {
                    TeamMemberResponse teamMembers = teamServiceClient.findByTeamsIdAndUsersId(boards.getTeamId(),boards.getUserId());
                    float importance = (float) workResponse.getImportance();
                    float workerNumber = (float) workResponse.getWorkerNumber();
                    teamMembers.setContribution(importance / workerNumber);
                    teamServiceClient.addContribution(teamMembers);


                }
                boards.setFeedbackYn(true);
                boardRepository.save(boards);
                //팀원 모두에게 피드백 완료 알람 전송
                allFeedbackCompleteAlarm(boards,workResponse);

                //workId에 해당하는 모든 게시판 피드백이 완료 되었을시 work의 status를 4로 변경
                AllWorkComplCheck(boards, workResponse);
            }
        }
    }


    //???? 완료 되었을 때도 알람이 가게 할지??
    public void AllWorkComplCheck(Boards boards, WorkResponse workResponse) {
        //피드백을 한 board의 work에 해당하는 모든 boardList들을 불러옴
        //work의 상태를 (4=완료)로 바꾸기 위해 work에 해당하는 boardList들의 feedbackYn이 모두 true인지 확인해야함
        List<Boards> boardList = boardRepository.findByWorksId(boards.getWorkId());

        boolean hasFeedbackYnTrue = true;

        // 모든 FeedbackStatuses의 feedback_yn이 true인지 확인
        for (Boards board : boardList) {
            if (board.isFeedbackYn() == false) { //한명이라도 feedback을 안했으면
                hasFeedbackYnTrue = false;
                break;
            }

            // 모든 BoardList들의 feedback_yn이 true라면 work의 status를 4로 변경
            //이미 status가 4하면 할 필요 없음
            if (hasFeedbackYnTrue && workResponse.getStatus() != 4) {


                workResponse.setStatus(4);
                workerServiceClient.updateWork(workResponse);

                //모든 work가 완료되었을때 알람을 보내도록 할지?
                //정하기
            }
        }
    }


    public void allFeedbackCompleteAlarm(Boards boards,WorkResponse workResponse) { //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감

        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목

        String message = "'[" + workName + "]" + title + "'에 대한 모든 피드백이 완료되어 '완료' 상태가 되었습니다.";
        String url = "/board/view/" + boards.getId();

        // 해당 팀에 속한 모든 멤버 가져와서 FeedbackStatuses에 추가
        List<MemberResponse> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        for (MemberResponse member : allMembers) {

            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setUserId(member.getId());
            alarmDTO.setContent(message);
            alarmDTO.setRedirectUrl(url);
            alarmDTO.setAlarmKind("complFeedback");
            alarmDTO.setBoardId(boards.getId());

            alarmServiceClient.createAlarm(alarmDTO);
        }

    }


    public static Feedbacks toEntity(FeedbackRequest feedbackRequest) {
        return Feedbacks.builder()
                .comment(feedbackRequest.getComment())
                .build();
    }


    public void reFeedback(Long boardId, Long memberId, Integer isApproved) { //재수락할지, 거절할지
        Boards boards = boardRepository.findBoardById(boardId);
        MemberResponse writers=memberServiceClient.findByUserId(memberId);
        WorkResponse workResponse=workerServiceClient.findWorkById(boards.getWorkId());
        //boards, writers로 feedbackStatus찾기
        FeedbackStatuses feedbackStatus = feedbackStatusRepository.findByBoardsIdAndUsersId(boardId, memberId);
        if (isApproved == 1) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatus.feedbackAgree();

            //게시판 작성자에게 수락 알람이 가도록 함
            reFeedbackAlarmAgree(boards, writers,workResponse);
        } else if ((isApproved == 2)) {
            // 거부한 경우 feedbackYn=false로 바꾸기
            feedbackStatus.feedbackDeny();

            //게시판 작성자에게 거절 알람이 가도록 함
            reFeedbackAlarmDeny(boards, writers,workResponse);
        }
        // 팀원 모두 동의하면 boards의 feedback_yn=true로 변경
        AllTeamMemberAgreeCheck(boards, workResponse);
    }


    public void reFeedbackAlarmDeny(Boards boards, MemberResponse writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getUserName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목

        List<MemberResponse> allMembers = teamServiceClient.findTeamById(boards.getTeamId());
        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'작성자님의 수정에 대해 거절을 하였습니다.";
        String url = "/board/view/" + boards.getId();
        for (MemberResponse member : allMembers) {
            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setUserId(member.getId());
            alarmDTO.setContent(message);
            alarmDTO.setRedirectUrl(url);
            alarmDTO.setAlarmKind("denyFeedback");
            alarmDTO.setBoardId(boards.getId());
            alarmDTO.setWriterPictureUrl(writers.getPictureUrl());
            alarmDTO.setWriterId(writers.getId());
            alarmServiceClient.createAlarm(alarmDTO);
        }

    }

    public void reFeedbackAlarmAgree(Boards boards, MemberResponse writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getUserName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목

        List<MemberResponse> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'작성자님의 수정에 대해 수락하였습니다.";
        String url = "/board/view/" + boards.getId();
        for (MemberResponse member : allMembers) {
            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setUserId(member.getId());
            alarmDTO.setContent(message);
            alarmDTO.setRedirectUrl(url);
            alarmDTO.setAlarmKind("agreeFeedback");
            alarmDTO.setBoardId(boards.getId());
            alarmDTO.setWriterPictureUrl(writers.getPictureUrl());
            alarmDTO.setWriterId(writers.getId());
            alarmServiceClient.createAlarm(alarmDTO);

        }
    }

    //피드백 불러오기
    //이중 반복문보다 좋은 방법이 있을지 생각해보기




}
