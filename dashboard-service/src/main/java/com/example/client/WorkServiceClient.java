package com.example.client;

import com.example.vo.WorkProgress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "work-service")
public interface WorkServiceClient {
    @GetMapping("/work-progress/{teamId}")
    WorkProgress getWorkProgress(@PathVariable Long teamId);
}
