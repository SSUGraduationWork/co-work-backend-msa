package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.domain.Alarms;
import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AlarmService {
    private AlarmRepository alarmRepository;


    //알람 리스트 처리리
    public List<AlarmDetailResponse> alarmList(Long memberId) {
        List<Object[]> results = alarmRepository.findAlarmsWithFeedbackYn(memberId);

        return results.stream()
                .map(result -> {
                    Alarms alarm = (Alarms) result[0];
                    Integer feedbackYn = (Integer) result[1];
                    AlarmDetailResponse response = AlarmDetailResponse.from(alarm);
                    response.setFeedbackYn(feedbackYn);
                    return response;
                })
                .collect(Collectors.toList());
    }


    // 알람을 확인했을때 seen=true로 업데이트
    public void updateSeenStatus(Long alarmId) {
        alarmRepository.updateSeenStatus(alarmId);
    }

}
