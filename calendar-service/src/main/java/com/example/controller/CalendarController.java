package com.example.controller;

import com.example.Exception.FormatBadRequestException;
import com.example.dto.MinutesForm;
import com.example.dto.RequestForm;
import com.example.entity.Minutes;
import com.example.service.CalendarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j //log
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //CORS
public class CalendarController {

    private final CalendarService calendarService;

    private class ResponseData {
        private int status;
        private String message;
        private Object data;

        public ResponseData(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }

    //회의록에서는 팀멤버임을 확인했다고 가정한 후의 API: team member임을 확인하는 과정 X
    //1: Calendar Minutes2
    //1-1. 회의록 생성: 해당 날짜의 회의록이 존재할 경우 해당 회의록 보여주기
    @PostMapping("/calendars/minutes/{teamId}")
    public ResponseEntity<ResponseData> createMinutes(@PathVariable Long teamId, @RequestBody RequestForm form) {
        System.out.println("form: "+form);
        //1. 원하는 정보가 모두 들어오지 않았을 경우 오류 발생
        boolean formcheck = calendarService.checkMinutesForm(teamId, form);
        if (formcheck == false) {
            throw new FormatBadRequestException("Fill In All");
        }

        //2. 팀아이디에 해당하는 회의록 생성
        MinutesForm minutesForm = calendarService.createMinutes(teamId, form);
        System.out.println("minutesPost: "+minutesForm);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success",minutesForm);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(1). 회의록 조회(전체)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/all/{teamId}")
    public ResponseEntity<ResponseData> getAllMinutes(@PathVariable Long teamId) {

        //전체 조회
        List<Minutes> minutesList = calendarService.watchAll(teamId);
        if (minutesList.isEmpty()) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutesList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(2). 회의록 부분 조회(특정 년도, 월)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/all/{teamId}/{yearMonth}")
    public ResponseEntity<ResponseData> getAllMinutes(@PathVariable Long teamId,@PathVariable String yearMonth) {
        // 부분 조회
        List<Minutes> minutesList = calendarService.watchDates(teamId, yearMonth);
        if (minutesList == null) {
            return null;
        }
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minutesList);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(3). 회의록 조회(세부)
    //조회할 것이 없을 경우 null 출력
    @GetMapping("/calendars/minutes/{teamId}/{date}")
    public ResponseEntity<ResponseData> getMinutes(@PathVariable Long teamId, @PathVariable String date) {

        //해당 teamId의 회의록 모두 찾기
        Minutes minute= calendarService.findMinute(teamId, date);
        minute.toDto(minute);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", minute);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-2(4). 회의록이 존재하는 모든 날짜리스트
    @GetMapping("/calendars/getExistMinutes/{teamId}/{currentMonth}")
    public ResponseEntity<ResponseData> getExistDate(@PathVariable Long teamId, @PathVariable String currentMonth) {
        //팀아이디에 해당하는 모든 minute 찾고 date 비교
        List<Integer> list = calendarService.findMinuteByDate(teamId, currentMonth);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", list);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-3. 회의록 수정 => 수정할때
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @PatchMapping("/calendars/minutes/{teamId}")
    public ResponseEntity<ResponseData> editMinutes(@PathVariable Long teamId, @RequestBody RequestForm form) {
        Minutes minutes = calendarService.findMinute(teamId, form.getDate());
        MinutesForm dtotarget = calendarService.editMinutes(minutes, form);

        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", dtotarget);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    //1-4. 회의록 삭제
    //해당 날짜가 존재하지 않을 경우 오류 발생
    @DeleteMapping("/calendars/minutes/{teamId}/{date}")
    public ResponseEntity<ResponseData> deleteMinutes(@PathVariable Long teamId, @PathVariable String date) {
        Minutes minutes = calendarService.findMinute(teamId, date);
        calendarService.deleteMinutes(minutes);
        ResponseData responseData = new ResponseData(HttpStatus.OK.value(), "Success", null);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
