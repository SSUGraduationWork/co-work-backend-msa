package com.example.demo.src.file.common;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonCode implements ResponseCode {

    GOOD_REQUEST(200, "올바른 요청입니다."),
    BAD_REQUEST(201, "잘못된 요청입니다."),
    ILLEGAL_REQUEST(202, "잘못된 데이터가 포함된 요청입니다."),
    VALIDATION_FAILURE(203, "입력값 검증이 실패하였습니다."),
    INTERNAL_SERVER_ERROR(404, "서버 내부에 문제가 생겼습니다."),
    ;

    private final int code;
    private final String message;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
