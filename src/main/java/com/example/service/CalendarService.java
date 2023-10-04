package com.example.service;

import com.example.dto.MinutesForm;
import com.example.dto.RequestForm;
import com.example.entity.Minutes;
import com.example.repository.MinutesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CalendarService {
    private final MinutesRepository minutesRepository;

    //확인
    //1-1. minutes not null 데이터를 모두 입력받았는지
    @Transactional
    public boolean checkMinutesForm(Long teamId, RequestForm form) {
        boolean checkform = true;
        if (teamId == null || form.getDate() == null) {
            checkform = false;
        }
        return checkform;
    }

    @Transactional
    public MinutesForm createMinutes(Long teamId, RequestForm dto) {
        //change
        Minutes minutes = new Minutes();
        minutes.create(teamId, dto.getDate(), dto.getTitle(), dto.getContent());
        minutesRepository.save(minutes);
        Minutes target = minutesRepository.findById(minutes.getMinutesId()).orElse(null);
        MinutesForm targetForm = target.toDto(target);

        return targetForm;
    }

    @Transactional
    public List<Minutes> watchAll(Long teamId) {
        List<Minutes> minutesList = minutesRepository.findByTeamId(teamId);
        return minutesList;
    }

    @Transactional
    public List<Minutes> watchDates(Long teamId, String filterMonth) {
        List<Minutes> target = new ArrayList<>();
        List<Minutes> minutesList = minutesRepository.findByTeamId(teamId);

        for (Minutes minutes : minutesList) {
            if (minutes.getDate().startsWith(filterMonth)) {
                target.add(minutes);
            }
        }
        return target;
    }
    @Transactional
    public Minutes findMinute(Long teamId, String date) {
        Minutes target = null;
        List<Minutes> minutesList = minutesRepository.findByTeamId(teamId);

        for (Minutes minutes : minutesList) {
            if (minutes.getDate().equals(date)) {
                target = minutes;
            }
        }
        return target;
    }

    @Transactional
    public List<Integer> findMinuteByDate(Long teamId, String currentMonth) {
        List<Integer> resultList = new ArrayList<>();
        List<Minutes> teamMinuteList = minutesRepository.findByTeamId(teamId);
        for (Minutes minutes : teamMinuteList) {
            if (String.valueOf(minutes.getDate()).startsWith(currentMonth)) {
                String[] parts = minutes.getDate().toString().split("-");
                String lastDigit = parts[2];
                Integer date = Integer.valueOf(lastDigit);
                resultList.add(date);
            }
        }
        return resultList;
    }

    @Transactional
    public MinutesForm editMinutes(Minutes minutes, RequestForm dto) {
        //현재 입력한 date가 repository에 존재할 경우 edit. orElseThrow()

        if (dto.getTitle() != null) {
            minutes.updateTitle(dto.getTitle());
        }

        if (dto.getContent() != null) {
            minutes.updateContent(dto.getContent());
        }

        MinutesForm dtotarget = minutes.toDto(minutes);

        return dtotarget;
    }

    @Transactional
    public void deleteMinutes(Minutes minutes) {
        minutesRepository.delete(minutes);
    }

}
