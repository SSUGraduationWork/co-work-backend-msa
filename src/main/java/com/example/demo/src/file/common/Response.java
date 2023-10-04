package com.example.demo.src.file.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Response<T> {

    private final Status status;
    private final T content;
    @Getter
    @Builder
    private static class Status {
        private int code;
        private String message;
    }

    public static <T> Response<T> of(ResponseCode responseCode, T content) {
        Status status = Status.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();

        return new Response<>(status, content);
    }

}
