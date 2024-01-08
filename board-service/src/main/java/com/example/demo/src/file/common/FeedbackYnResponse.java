package com.example.demo.src.file.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FeedbackYnResponse<T> {

    private final Status status;
    private final T content;
    private final int feedbackYn;
    private  final String pictureUrl;
    private  final String userName;
    private final int studentNumber;
    @Getter
    @Builder
    private static class Status {
        private int code;
        private String message;
    }


    public static <T> FeedbackYnResponse<T> of(ResponseCode responseCode, T content, int feedbackYn, String pictureUrl,String userName,int studentNumber) {
        Status status = Status.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build();

        return new FeedbackYnResponse<>(status, content, feedbackYn,pictureUrl,userName,studentNumber);
    }

}
