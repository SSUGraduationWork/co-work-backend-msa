package com.example.demo.src.file.Service;

import com.example.demo.src.file.Repository.AlarmRepository;
import com.example.demo.src.file.domain.Alarms;

import com.example.demo.src.file.dto.response.AlarmDetailResponse;
import com.example.demo.src.file.vo.AlarmDTO;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.Environment;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AlarmService {
    private AlarmRepository alarmRepository;




        //알람 리스트 처리리
    public List<AlarmDetailResponse> alarmList(Long memberId) {
        List<Alarms> results = alarmRepository.findAlarmsWithFeedbackYn(memberId);

        return results.stream()
                .map(result -> {
                    Alarms alarm = (Alarms) result;
                    AlarmDetailResponse response = AlarmDetailResponse.from(alarm);
                    return response;
                })
                .collect(Collectors.toList());
    }


    // 알람을 확인했을때 seen=true로 업데이트
    public void updateSeenStatus(Long alarmId) {
        alarmRepository.updateSeenStatus(alarmId);
    }


    public void createAlarm(@NotNull AlarmDTO alarmDTO) {
        // AlarmDTO를 Alarm으로 변환하여 저장

        Alarms alarms=new Alarms();
        alarms.setContent(alarmDTO.getContent());
        alarms.setWriterPictureUrl(alarmDTO.getWriterPictureUrl());
        alarms.setContent(alarmDTO.getContent());
        alarms.setRedirectUrl(alarmDTO.getRedirectUrl());
        alarms.setAlarmKind(alarmDTO.getAlarmKind());
        alarms.setBoardId(alarmDTO.getBoardId());
        alarms.setWriterId(alarmDTO.getWriterId());
        alarms.setUserId(alarmDTO.getUserId());
        alarms.setSeen(false);
       alarmRepository.save(alarms);
    }



    public void createAlarmList(List<AlarmDTO> alarmDTOList) {
        List<Alarms> alarmsToSave = alarmDTOList.stream()
                .map(this::toEntity) // toEntity 메서드를 사용하여 AlarmDTO를 Alarms로 매핑
                .collect(Collectors.toList());
        alarmRepository.saveAll(alarmsToSave); // Alarms 객체를 저장
    }

    public Alarms toEntity(AlarmDTO alarmDTO) {
        Alarms alarms=new Alarms();
        alarms.setWriterPictureUrl(alarmDTO.getWriterPictureUrl());
        alarms.setContent(alarmDTO.getContent());
        alarms.setRedirectUrl(alarmDTO.getRedirectUrl());
        alarms.setAlarmKind(alarmDTO.getAlarmKind());
        alarms.setBoardId(alarmDTO.getBoardId());
        alarms.setWriterId(alarmDTO.getWriterId());
        Alarms alarm = toEntity(alarmDTO);
        return alarms;
    }



}
