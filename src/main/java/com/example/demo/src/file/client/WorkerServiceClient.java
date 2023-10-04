package com.example.demo.src.file.client;


import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.vo.WorkResponse;
import com.example.demo.src.file.vo.WorkerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name="work-service" )
public interface WorkerServiceClient {

    @GetMapping("/work-service/work/find/{memberId}/{workId}")
    WorkerResponse findByUsersIdAndWorksId(@PathVariable("memberId") Long memberId,
                                           @PathVariable("workId") Long workId);


    @PostMapping("/work-service/work/setTrue/{memberId}/{workId}")
    void setTrue(@PathVariable("memberId") Long memberId,
                                           @PathVariable("workId") Long workId);

    @GetMapping("/work-service/worklist/{teamId}")
    List<WorkResponse> findWorksByTeamId(@PathVariable("teamId") Long teamId);

    @GetMapping("/work-service/work/find/{workId}")
    WorkResponse findWorkById(@PathVariable Long workId);

    @PostMapping("/work-service/work/set")
    void updateWork(@RequestBody  Integer integer);

}
