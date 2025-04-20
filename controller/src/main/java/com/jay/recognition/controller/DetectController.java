package com.jay.recognition.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jay.recognition.common.Result;
import com.jay.recognition.domain.detect.dto.ImageDTO;
import com.jay.recognition.domain.detect.service.DetectService;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Log4j
@CrossOrigin(origins = "*")
public class DetectController {
    @Autowired
    DetectService detectService;
    private static final Logger logger = LoggerFactory.getLogger(DetectController.class);

    @PostMapping("/det")
    public @ResponseBody ResponseEntity<Resource> detectImg(
            @RequestParam("image") MultipartFile image,
            @RequestParam("IOU") float iou,
            @RequestParam("confidence") float confidence,
            @RequestParam("model") String model) throws JsonProcessingException {
        ImageDTO imageDTO = new ImageDTO(image, iou, confidence, model);
        logger.info(imageDTO.toString());
        return detectService.detect(imageDTO);
    }

    @PostMapping("/test")
    public @ResponseBody String forTest() throws JsonProcessingException {
        logger.info("test");
        Result<String> test = Result.success("test");
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(test);
        return s;
    }
}
