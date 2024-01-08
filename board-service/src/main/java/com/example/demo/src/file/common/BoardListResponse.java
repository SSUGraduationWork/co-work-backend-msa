package com.example.demo.src.file.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class BoardListResponse<T> {

    private final Status status;
    private final T content;
    private final T work;
    private  final T member;

    @Getter
    @Builder
    private static class Status {
        private int code;
        private String message;
    }


    public static <T> BoardListResponse<T> of(ResponseCode responseCode, T content, T work, T member) {
        Status status = Status.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();

        return new BoardListResponse<>(status, content, work, member);
    }

}
