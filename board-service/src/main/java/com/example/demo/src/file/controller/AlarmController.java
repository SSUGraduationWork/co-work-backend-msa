package com.example.demo.src.file.controller;

import com.example.demo.src.file.Service.AlarmService;
import com.example.demo.src.file.common.CommonCode;
import com.example.demo.src.file.common.Response;
import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;


    //멤버별로 알람 리스트 확인하기
    @GetMapping("/alarmList/view/{memberId}")
    public ResponseEntity<Response<List<AlarmDetailResponse>>> AlarmList(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, alarmService.alarmList(memberId)));
    }

    @PutMapping("/updateSeenStatus/{alarmId}")
    public ResponseEntity<String> updateSeenStatus(@PathVariable Long alarmId) {
        alarmService.updateSeenStatus(alarmId);
        return ResponseEntity.ok("Seen status updated successfully");
    }
}


