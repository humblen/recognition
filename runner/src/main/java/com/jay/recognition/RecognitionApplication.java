package com.jay.recognition;

import com.jay.recognition.controller.DetectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RecognitionApplication {

    @Autowired
    DetectController detectController;
    public static void main(String[] args) {
        SpringApplication.run(RecognitionApplication.class, args);
    }

}
