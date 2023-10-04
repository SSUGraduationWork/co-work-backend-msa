package com.example.demo.src.file.client;

import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.vo.MemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="user-service" )
public interface MemberServiceClient {

    @GetMapping("/user-service/find/userId/{userId}")
    MemberResponse findByUserId(@PathVariable Long userId);

}
