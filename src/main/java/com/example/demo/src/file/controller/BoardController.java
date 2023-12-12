//코드 수정할거 request,response DTO로 변환
package com.example.demo.src.file.controller;

import com.example.demo.src.file.Repository.FileRepository;
import com.example.demo.src.file.Service.BoardService;
import com.example.demo.src.file.common.BoardListResponse;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.request.BoardWriteRequest;
import com.example.demo.src.file.dto.response.BoardDetailResponse;
import com.example.demo.src.file.dto.response.BoardResponse;
import com.example.demo.src.file.dto.response.PostsResponse;
import com.example.demo.src.file.dto.response.multiWriteResponse;
import com.example.demo.src.file.vo.*;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/")
public class BoardController {

    private final BoardService boardService;
    private FileRepository fileRepository;


    //파일 한번에 여러게 업로드 및 게시글 작성
    @Transactional
    @PostMapping("/board/upcount/{boardId}")
    public void upcount( @PathVariable("boardId") Long boardId ) {
        //조회수 증가 로직
        boardService.increaseCount(boardId);
    }

    //파일 한번에 여러게 업로드 및 게시글 작성
    @Transactional
    @PostMapping("/board/multiWrite/{memberId}/{teamId}/{workId}")
    public ResponseEntity<Response<multiWriteResponse>> multipleBoardWriteForm(@Valid BoardWriteRequest request,
                                                                               @PathVariable("memberId") Long memberId,
                                                                               @PathVariable("teamId") Long teamId,
                                                                               @PathVariable("workId") Long workId,
                                                                               @PathVariable(value = "files", required = false) MultipartFile[] files) throws Exception{
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST,boardService.multiWrite(request,memberId,teamId,workId,files)));
    }


    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/board/list/{memberId}/{teamId}")
    public ResponseEntity<BoardListResponse<?>> boardList(
            @PathVariable("memberId") Long memberId,
            @PathVariable("teamId") Long teamId) {

        List<BoardResponse> boardResponses = boardService.boardList(memberId, teamId);

        List<BoardWorkDto> workResponses=boardService.workList(teamId);
        List<ResponseTeamMember> memberResponses=boardService.teamMemberList(teamId);

        return ResponseEntity.ok(BoardListResponse.of(CommonCode.GOOD_REQUEST, boardResponses,workResponses,memberResponses));
    }

    //게시글 리스트+페이징 추후 필요+정렬 필요
    @GetMapping("/professor/board/list/{teamId}")
    public ResponseEntity<BoardListResponse<?>> professorBoardList(
            @PathVariable("teamId") Long teamId) {

        List<BoardResponse> boardResponses = boardService. professorBoardList(teamId);

        List<BoardWorkDto> workResponses=boardService.workList(teamId);
        List<ResponseTeamMember> memberResponses=boardService.teamMemberList(teamId);

        return ResponseEntity.ok(BoardListResponse.of(CommonCode.GOOD_REQUEST, boardResponses,workResponses,memberResponses));
    }


    //특정 게시글 눌렀을 때 상세 페이지 생성
    @GetMapping("/board/view/{boardId}/{memberId}/{teamId}")
    public ResponseEntity<Response<BoardDetailResponse>> boardDetailView(@PathVariable("boardId") Long id,@PathVariable("memberId") Long memberId,
                                                                         @PathVariable("teamId") Long teamId){

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.boardView(id)));
    }

    //게시판 삭제
    @DeleteMapping("board/delete/{boardId}")
    public String boardDelete(@PathVariable("boardId") Long id){

       boardService.boardDelete(id);
       return "삭제 성공";
    }



    //다중 파일 게시판 수정
    @PostMapping("/multiboard/update/{boardId}/{memberId}/{teamId}/{workId}") //mod_compl, 수정을 완료했는지
    public ResponseEntity<Response<multiWriteResponse>> multiboardModify(@PathVariable("boardId") Long boardId,
                                                                    @PathVariable("memberId") Long memberId,
                                                                    @PathVariable("teamId") Long teamId,
                                                                    @PathVariable("workId") Long workId,
                                                               @Valid BoardWriteRequest request,
                                                               @PathVariable(value = "files", required = false) MultipartFile[] files) throws Exception {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, boardService.multiReWrite(boardId,workId,request, files )));
    }

    //workId에 해당하는 모든 게시글 반환
    @GetMapping("/board/posts/{workId}")
    public ResponseEntity<List<PostsResponse>> getPostsByWorkId(@PathVariable("workId") Long workId){
        List<PostsResponse> postsByWorkId = boardService.getPosts(workId);

        return ResponseEntity.status(HttpStatus.OK).body(postsByWorkId);
    }
}


