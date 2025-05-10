package com.jay.recognition.controller;


import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.search.dto.SearchDTO;
import com.jay.recognition.domain.search.service.SearchService;
import com.jay.recognition.domain.search.service.impl.SearchServiceImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Log4j
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("/search")
    public Map<String, Object> search(@RequestParam String driver, @RequestParam String location, @RequestParam String startTime, @RequestParam String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (startTime == null || startTime.isEmpty()) startTime = "2000-01-01T00:00";
        if (endTime == null || endTime.isEmpty()) endTime = "2999-01-01T00:00";
        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);
        SearchDTO searchDTO = new SearchDTO(driver, location, startDate, endDate);
        Map<String, Object> search = searchService.search(searchDTO);
        return search;
    }
}
