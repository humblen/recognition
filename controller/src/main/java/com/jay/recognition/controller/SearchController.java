package com.jay.recognition.controller;


import com.jay.recognition.domain.search.dto.SearchDTO;
import com.jay.recognition.domain.search.service.SearchService;
import com.jay.recognition.domain.search.service.impl.SearchServiceImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Log4j
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("/search")
    public void search(@RequestParam String driver, @RequestParam String location, @RequestParam String startTime, @RequestParam String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);
        SearchDTO searchDTO = new SearchDTO(driver, location, startDate, endDate);
        searchService.search(searchDTO);
    }
}
