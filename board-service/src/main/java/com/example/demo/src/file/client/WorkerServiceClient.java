package com.example.demo.src.file.client;

import com.example.demo.src.file.vo.BoardWorkDto;
import com.example.demo.src.file.vo.WorkResponse;
import com.example.demo.src.file.vo.WorkerResponse;
import com.example.demo.src.file.vo.WorkersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(name="work-service" )
public interface WorkerServiceClient {

    @GetMapping("/works/write-status/{userId}/{workId}")
    WorkerResponse getWriteStatus(@PathVariable("userId") Long userId,
                                           @PathVariable("workId") Long workId);


    @PostMapping("/works/write-status/{userId}/{workId}")
    void setWriteStatusTrue(@PathVariable("userId") Long userId,
                                           @PathVariable("workId") Long workId);

    @GetMapping("/works-list/{teamId}")
    List<BoardWorkDto> findWorksByTeamId(@PathVariable("teamId") Long teamId);

    @GetMapping("/work/{workId}")
    WorkResponse findWorkById(@PathVariable Long workId);

    @PostMapping("/works/status/{workId}/{status}")
    void setWorkStatus(@PathVariable("workId") Long workId, @PathVariable("status") Integer status);

    @GetMapping("/worker/{workId}")
    List<WorkersResponse> findWorkerById(@PathVariable Long workId);
}
