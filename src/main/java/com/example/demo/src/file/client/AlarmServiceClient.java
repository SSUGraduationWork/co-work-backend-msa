package com.example.demo.src.file.client;


import com.example.demo.src.file.vo.AlarmDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="alarm-service" )
public interface AlarmServiceClient {

    @PostMapping("/alarm-service/alarms/create")
    void createAlarm(@RequestBody AlarmDTO alarmDTO);


}
