package com.jay.recognition.domain.detect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDTO {
    private MultipartFile image;
    private Float IOU;
    private Float confidence;
    private String model;
}
