package com.example.demo.src.file.controller;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.Service.AlarmService;

import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import com.example.demo.src.file.vo.AlarmDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
//@CrossOrigin(origins="http://localhost:8081")
@RequestMapping("/alarm-service")
public class AlarmController {

    private final AlarmService alarmService;

    private final AlarmRepository alarmRepository;
    //멤버별로 알람 리스트 확인하기
    @GetMapping("/alarmList/view/{memberId}")
    public ResponseEntity<List<AlarmDetailResponse>> AlarmList(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok( alarmService.alarmList(memberId));
    }

    @PutMapping("/updateSeenStatus/{alarmId}")
    public ResponseEntity<String> updateSeenStatus(@PathVariable Long alarmId) {
        alarmService.updateSeenStatus(alarmId);
        return ResponseEntity.ok("Seen status updated successfully");
    }


    @PostMapping("/alarms/create")
    public void createAlarm(@RequestBody AlarmDTO alarmDTO) {
        alarmService.createAlarm(alarmDTO);
    }

    @PostMapping("/alarms/sss")
    public void createAlarmList() {
        Alarms alarms=new Alarms();
        alarms.setContent("ddd");
        alarms.setAlarmKind("dfdf");
        alarms.setWriterId(3L);
        alarms.setBoardId(7L);
        alarms.setUserId(2L);
        alarms.setRedirectUrl("232");
        alarms.setWriterPictureUrl("23");
        alarmRepository.save(alarms);
        System.out.println(alarms.getAlarmId());
    }

}


