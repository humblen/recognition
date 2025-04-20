package com.jay.recognition.domain.search.service.impl;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.search.dto.SearchDTO;
import com.jay.recognition.domain.search.service.SearchService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class SearchServiceImpl implements SearchService {
    @Override
    public List<Detection> search(SearchDTO searchDTO) {

    }
}
