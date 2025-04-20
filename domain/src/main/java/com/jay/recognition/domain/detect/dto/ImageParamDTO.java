package com.jay.recognition.domain.detect.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageParamDTO {
    private String path;
    private Float IOU;
    private Float confidence;
    private String model;
    private String savePath;
}
