package com.example.demo.src.file.Service;


import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Repository.BoardRepository;
import com.example.demo.src.file.Repository.FeedbackRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.client.*;
import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.domain.Boards;
import com.example.demo.src.file.domain.FeedbackStatuses;
import com.example.demo.src.file.domain.Feedbacks;
import com.example.demo.src.file.dto.request.FeedbackRequest;
import com.example.demo.src.file.dto.response.FeedbackResponse;

import com.example.demo.src.file.dto.response.OneBoardIdResponse;
import com.example.demo.src.file.vo.ResponseTeamMember;
import com.example.demo.src.file.vo.TeamMemberResponse;
import com.example.demo.src.file.vo.WorkResponse;
import com.example.demo.src.file.vo.WorkersResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FeedbackService {


    private FeedbackRepository feedbackRepository;
    private BoardRepository boardRepository;
    private FeedbackStatusRepository feedbackStatusRepository;
    private AlarmRepository alarmRepository;
    @Autowired
    WorkerServiceClient workerServiceClient;

    @Autowired
    TeamServiceClient teamServiceClient;
    @Autowired
    MemberServiceClient memberServiceClient;


    public FeedbackResponse save(Long boardId, Long memberId, FeedbackRequest requset, Integer isApproved) {
        //피드백 등록
        Boards boards = boardRepository.findBoardById(boardId);
        ResponseTeamMember writers = memberServiceClient.findByUserId(memberId);//피드백 작성자
        WorkResponse workResponse=workerServiceClient.findWorkById(boards.getWorkId());
        // FeedbackStatuses feedbackStatuses = feedbackStatusRepository.findByBoardsIdAndUsersId(boards.getId(), writers.getId());

        Feedbacks feedbacks = toEntity(requset);
        feedbacks.confirmBoard(boards);
        feedbacks.setWriterId(writers.getId());

        OneTeaamMemberAgreeCheck(feedbacks,isApproved, boards, writers,workResponse);

        // 팀원 모두 동의하면 boards의 feedback_yn=true로 변경
        AllTeamMemberAgreeCheck(boards,workResponse);

        return FeedbackResponse.from(feedbacks, boards,writers);
    }

    public void OneTeaamMemberAgreeCheck(Feedbacks feedbacks,Integer isApproved, Boards boards, ResponseTeamMember writers,WorkResponse workResponse) {


        //-----------------------------
        FeedbackStatuses feedbackStatuses = feedbackStatusRepository.findByBoardsIdAndUsersId(boards.getId(), writers.getId());
        //한번도 피드백을 이미 한 경우 예외처리
        if (feedbackStatuses.getFeedbackYn() != 0||feedbackStatuses.getFeedbackYn() == 3) {
            //피드백을 이미 한 경우에는 피드백 메시지를 달았던 당시 피드백 상태로 보여주기 위함
            feedbacks.setModReq(feedbackStatuses.getFeedbackYn());
            feedbackRepository.save(feedbacks);
            return;
        }


        /*// works.getEndDate()가 현재 시간보다 이후인지 확인합니다.
        //마감기한 전에 피드백 했을 때만 점수 받을 수 있음
        //writer에게 기여도 +1증가, 게시판 작성자는 피드백 점수에서 제외
        LocalDateTime currentTime = LocalDateTime.now();
        // 작업이 종료되지 않은 경우
        long hoursDifference = ChronoUnit.HOURS.between(currentTime, workResponse.getEndDate());
        if (workResponse.getEndDate().isAfter(currentTime)) {
            TeamMemberResponse teamMembers = teamServiceClient.findByTeamsIdAndUsersId(boards.getTeamId(),writers.getId());
            teamMembers.setContribution(1);
            teamServiceClient.addContribution(teamMembers);
        } else if (hoursDifference <= 24) {

        }*/


        //피드백 작성자가 동의 하면 feedbackStauses의 feedback_yn=true로 변경
        //중복 코드 수정, feedbackStatus조회
        if (isApproved == 1) {
            // 승인한 경우 feedbackYn=true로 바꾸기
            feedbackStatuses.feedbackAgree();
            // 피드백을 처음 달았고 승인한 경우 modReq=true로 바꾸기
            feedbacks.feedbackAgree();
        } else if ((isApproved == 2)) {
            // 피드백을 처음 달았고 거부한 경우 modReq=false로 바꾸기
            feedbackStatuses.feedbackDeny();
            feedbacks.feedbackDeny();
            //피드백에서 수정 요청 시  수정 요청한 본인 제외 모든 팀의 모든 팀원들에게 알람이 감
            FeedbackStatusAndAlarm(boards, writers,workResponse);

        }
        feedbackRepository.save(feedbacks);
        feedbackStatusRepository.save(feedbackStatuses);
    }

    //멤버수에 맞는 feedbackstatus 테이블 등록 및 글 생성 알람 메시지 저장
    public void FeedbackStatusAndAlarm(Boards boards, ResponseTeamMember writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //피드백에서 수정 요청 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목


        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'에 대해 수정 요청을 하였습니다.";
        String url = "/board/view/" + boards.getId();
        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        for (ResponseTeamMember member : allMembers) {

            if (member.equals(writers)) {
                continue;
            }


            Alarms alarms = new Alarms();
            alarms.setUserId(member.getId()); // 연관관계 설정, board의 작성자로 저장
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setAlarmKind("requestFeedback");
            alarms.setBoardId(boards.getId());
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);
        }
    }


    //게시글 당 피드백 완료시 작업 점수
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


                // 작성자가 마감일 전에 작성했으면 점수를 받을 수 있음
                //마감기한 전에 피드백 했을 때만 점수 받을 수 있음
                long hoursDifference = ChronoUnit.HOURS.between( workResponse.getEndDate(),boards.getCreatedAt());
               // System.out.println("시간 차이"+hoursDifference);
                if (workResponse.getEndDate().isAfter(boards.getCreatedAt())) {
                //    System.out.println("마감일 이내");
                    TeamMemberResponse teamMembers = teamServiceClient.findByTeamsIdAndUsersId(boards.getTeamId(),boards.getUserId());
                    int importance =  workResponse.getImportance();
                    teamMembers.setContribution(importance);
                    teamServiceClient.addContribution(teamMembers);
                }else if (0<hoursDifference && hoursDifference <= 24) {// 작성자가 마감일 전에 작성했으면 점수를 절반만 받을 수 있음
                //    System.out.println("24시간 이내");
                    TeamMemberResponse teamMembers = teamServiceClient.findByTeamsIdAndUsersId(boards.getTeamId(),boards.getUserId());
                    float importance =  workResponse.getImportance();
                    teamMembers.setContribution(importance/2);
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


    //완료 작업 status 4
    public void AllWorkComplCheck(Boards boards, WorkResponse workResponse) {

        List<WorkersResponse> workersResponseList =workerServiceClient.findWorkerById(workResponse.getId());

        List<Long> userIdList = workersResponseList.stream()
                .map(WorkersResponse::getUserId)
                .collect(Collectors.toList());

        List<OneBoardIdResponse> boardList =boardRepository.findByUserIdInAndWorkId(userIdList,workResponse.getId());

        for(OneBoardIdResponse one:boardList ){
           System.out.println("boardId  "+one);
        }

        System.out.println("boardIdSize  "+boardList.size());
        System.out.println("userIdSize  "+userIdList.size());
        boolean hasFeedbackYnTrue = true;




        // 모든 FeedbackStatuses의 feedback_yn이 true인지 확인
        for (OneBoardIdResponse board : boardList) {
            System.out.println("실행 test");
            //올린 게시물 하나라도 피드백 완료상태가 아니거나, 한 작업의 담당자수와 작성된 게시물 수가 다르면 완료(4)가 아님
            if (board.isFeedbackYn() == false||(boardList.size()!=userIdList.size())) { //한명이라도 feedback을 안했으면
                hasFeedbackYnTrue = false;
                break;
            }
            System.out.println("실행 test2");
            // 모든 BoardList들의 feedback_yn이 true라면 work의 status를 4로 변경
            //이미 status가 4하면 할 필요 없음
            if (hasFeedbackYnTrue && workResponse.getStatus() != 4) {


                workerServiceClient.setWorkStatus(workResponse.getId(), 4);

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
        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        for (ResponseTeamMember member : allMembers) {

            Alarms alarms = new Alarms();
            alarms.setUserId(member.getId());// 연관관계 설정, board의 작성자로 저장
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("complFeedback");
            alarms.setBoardId(boards.getId());
            alarmRepository.save(alarms);
        }

    }


    public static Feedbacks toEntity(FeedbackRequest feedbackRequest) {
        return Feedbacks.builder()
                .comment(feedbackRequest.getComment())
                .build();
    }


    public void reFeedback(Long boardId, Long memberId, Integer isApproved) { //재수락할지, 거절할지
        Boards boards = boardRepository.findBoardById(boardId);
        ResponseTeamMember writers=memberServiceClient.findByUserId(memberId);
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


    public void reFeedbackAlarmDeny(Boards boards, ResponseTeamMember writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목

        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());
        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'작성자님의 수정에 대해 거절을 하였습니다.";
        String url = "/board/view/" + boards.getId();
        System.out.println("writers"+writers);
        for (ResponseTeamMember member : allMembers) {

            if(member.getId().equals(writers.getId())){

                continue;}
            Alarms alarms = new Alarms();
            alarms.setUserId(member.getId()); // 연관관계 설정, board의 작성자로 저장->모든 멤버들한테 가도록
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("denyFeedback");
            alarms.setBoardId(boards.getId());
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);
        }

    }

    public void reFeedbackAlarmAgree(Boards boards, ResponseTeamMember writers,WorkResponse workResponse) { //wrtier은 feedback작성자

        //알림 기능, 글 등록 시 모든 팀의 모든 팀원들에게 알람이 감
        String userName = writers.getName(); //피드백 작성자 이름
        Integer studentNumber = writers.getStudentNumber(); // 피드백 작성자 학번
        String workName = workResponse.getWorkName();//작업이름
        String title = boards.getTitle(); //게시판 제목

        List<ResponseTeamMember> allMembers = teamServiceClient.findTeamById(boards.getTeamId());

        String message = "'" + studentNumber + " " + userName + "'님께서 '[" + workName + "]" + title + "'작성자님의 수정에 대해 수락하였습니다.";
        String url = "/board/view/" + boards.getId();
        for (ResponseTeamMember member : allMembers) {

            if(member.getId().equals(writers.getId())){continue;}
            Alarms alarms = new Alarms();
            alarms.setUserId(member.getId()); // 연관관계 설정, board의 작성자로 저장
            alarms.setContent(message);
            alarms.setRedirectUrl(url);
            alarms.setAlarmKind("agreeFeedback");
            alarms.setBoardId(boards.getId());
            alarms.setWriterPictureUrl(writers.getPictureUrl());
            alarms.setWriterId(writers.getId());
            alarmRepository.save(alarms);

        }
    }

    //피드백 불러오기
    //이중 반복문보다 좋은 방법이 있을지 생각해보기




}
