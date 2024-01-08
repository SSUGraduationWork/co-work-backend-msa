package com.example.dto;

import com.example.vo.UserContribution;
import com.example.vo.WorkProgress;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ContributionRes {
    private WorkProgress progress;
    private List<UserContribution> contribution;
}
