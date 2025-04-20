package com.jay.recognition.domain.search.service;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.search.dto.SearchDTO;

import java.util.List;

public interface SearchService {
    List<Detection> search(SearchDTO searchDTO);
}
