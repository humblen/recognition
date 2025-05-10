package com.jay.recognition.domain.search.service;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.search.dto.SearchDTO;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SearchService {
    Map<String, Object> search(SearchDTO searchDTO);
}
