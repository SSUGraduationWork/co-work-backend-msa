package com.example.client;

import com.example.dto.UserDto;
import com.example.vo.Contribution;
import com.example.vo.UserContribution;
import com.example.vo.UserIdList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @PostMapping("/users")
    List<UserDto> getUsersInfo(@RequestBody UserIdList userIdList);

    @PostMapping("/users/contribution")
    List<UserContribution> getUsersContribution(@RequestBody List<Contribution> userList);
}
