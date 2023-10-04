package com.example.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//not null인 정보가 모두 들어오지 않았을 경우의 오류 처리
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FormatBadRequestException extends RuntimeException{
    public FormatBadRequestException(String message) {
        super(message);
    }
}
