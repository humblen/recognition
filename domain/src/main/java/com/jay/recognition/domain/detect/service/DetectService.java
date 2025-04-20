package com.jay.recognition.domain.detect.service;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.detect.dto.ImageDTO;
import com.jay.recognition.domain.detect.dto.ImageParamDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface DetectService {
    ResponseEntity<Resource> detect(ImageDTO imageDTO);

    String saveImage(MultipartFile image);

    String detectImage(ImageParamDTO imageParamDTO);

    void saveDetectResult(Detection detection);

    void detectTrack(Path path) throws IOException;
}
