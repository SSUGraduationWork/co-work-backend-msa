package com.example.controller;

import com.example.dto.ContributionRes;
import com.example.service.ContributionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class ContributionController {
    private final ContributionService contributionService;

    //1. contribution 조회
    @GetMapping("/contributions/{teamId}")
    public ResponseEntity<ContributionRes> getContributions(@PathVariable("teamId") Long teamId){
        ContributionRes result = contributionService.getContributionInfo(teamId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
