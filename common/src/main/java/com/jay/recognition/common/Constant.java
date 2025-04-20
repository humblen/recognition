package com.jay.recognition.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Constant {

    @Getter
    @AllArgsConstructor
    public enum Response {

        SUCCESS(200, "success"),
        FAIL(500, "fail");

        private final Integer CODE;
        private final String INFO;
    }
}
