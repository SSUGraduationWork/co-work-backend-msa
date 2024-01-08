package com.example.demo.src.file.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CommentResponse<T> {

    private final Status status;
    private final T feedback;
    private final T feedbackStatus;
    private  final T writer;

    @Getter
    @Builder
    private static class Status {
        private int code;
        private String message;
    }


    public static <T> CommentResponse<T> of(ResponseCode responseCode, T feedback, T feedbackStatus, T writer) {
        Status status = Status.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();

        return new CommentResponse<>(status, feedback, feedbackStatus, writer);
    }

}
