package com.jay.recognition.domain.detect.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detection {
    private Integer id;
    private String driver;
    private Integer status;
    private String location;
    private LocalDateTime detectTime;

    public Detection(String driver, Integer status, String location, LocalDateTime detectTime) {
        this.driver = driver;
        this.status = status;
        this.location = location;
        this.detectTime = detectTime;
    }
}
