package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class UserIdList {
    private List<Long> userIdList;
    public UserIdList(){

    }
}
