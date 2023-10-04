package com.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="member-service", url="?")
public interface MemberClient {

//    @GetMapping("/getMember")
//    String getMember;

}
