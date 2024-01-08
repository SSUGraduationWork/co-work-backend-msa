package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserIdList {
    private List<Long> userIdList;
    public UserIdList() {

    }
}
