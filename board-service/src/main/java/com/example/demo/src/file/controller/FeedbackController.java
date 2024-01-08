package com.example.demo.src.file.controller;

import com.example.demo.src.file.Repository.FeedbackRepository;
import com.example.demo.src.file.Repository.FeedbackStatusRepository;
import com.example.demo.src.file.Service.BoardService;
import com.example.demo.src.file.Service.FeedbackService;
import com.example.demo.src.file.client.TeamServiceClient;
import com.example.demo.src.file.common.CommentResponse;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.request.FeedbackRequest;
import com.example.demo.src.file.dto.response.BoardFeedbackResponse;
import com.example.demo.src.file.dto.response.FeedbackResponse;
import com.example.demo.src.file.dto.response.FeedbackStatusFeedbackYnUserIdResponse;
import com.example.demo.src.file.dto.response.FeedbackStatusTeamMember;

import com.example.demo.src.file.vo.ResponseTeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final FeedbackStatusRepository feedbackStatusRepository;

    private final FeedbackRepository feedbackRepository;
    private final BoardService boardService;
    @Autowired
    TeamServiceClient teamServiceClient;
    //피드백 글쓰기
    //isApproved=0인경우 피드백 거부, isApproved=1인경우 피드백 승인

    @PostMapping("/comment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<FeedbackResponse>>  commentSave(@PathVariable("boardId") Long boardId,
                                                                   @PathVariable("writerId") Long writerId,
                                                                   @RequestBody FeedbackRequest request,
                                                                   @PathVariable("isApproved") Integer isApproved){
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, feedbackService.save(boardId,writerId,request,isApproved)));
    }
    //피드백 반영하여 수정한 게시판에 대한 재수락, 재수정
    @PostMapping("/recomment/{boardId}/{writerId}/{isApproved}")
    @ResponseStatus(HttpStatus.CREATED)
    public void  reApproved(@PathVariable("boardId") Long boardId,
                            @PathVariable("writerId") Long writerId,
                            @PathVariable("isApproved") Integer isApproved){
        feedbackService.reFeedback(boardId,writerId,isApproved);
    }


    //피드백 보기
    //front에서 피드백을 쓰자마자 바로 보여주기 위해서 addComment라는 변수에 담아서 화면에 보여줬음.
    //feedback의 거절 승인은 한번하여 번복이 없음. feedback 승인,여부가 필요함
    @GetMapping("/comment/{boardId}/{teamId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentResponse<List<?>>>  commentFeedbackView(@PathVariable("boardId") Long boardId,
                                                                         @PathVariable("teamId") Long teamId){
    //    Members members=feedbackService.getUsers(memberId);
        List<FeedbackStatusFeedbackYnUserIdResponse> feedbackStatusesList = feedbackStatusRepository.findFeedbackYnAndUserIdByBoardsId(boardId);
        System.out.println(feedbackStatusesList.get(0).getFeedbackYn());
        List<ResponseTeamMember> memberResponses=boardService.teamMemberList(teamId);
        System.out.println(memberResponses.get(0).getName());
        List<BoardFeedbackResponse> feedbacksList = feedbackRepository.findFeedbackByBoardsId(boardId);
        System.out.println("hello");

        return  ResponseEntity.ok(CommentResponse.of(CommonCode.GOOD_REQUEST, feedbacksList,feedbackStatusesList,memberResponses));
    }



}
