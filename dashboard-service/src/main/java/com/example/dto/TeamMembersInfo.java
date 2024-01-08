package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamMembersInfo {
    private List<UserDto> teamMembers;
}
