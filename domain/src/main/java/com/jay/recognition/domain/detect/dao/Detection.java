package com.jay.recognition.domain.detect.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Detection {
    private Integer id;
    private String driver;
    private Integer status;
    private String location;
    private Date detectTime;

    public Detection(String driver, Integer status, String location, Date detectTime) {
        this.driver = driver;
        this.status = status;
        this.location = location;
        this.detectTime = detectTime;
    }
}
