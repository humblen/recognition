package com.jay.recognition.domain.detect.mapper;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.search.dto.SearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Mapper
public interface DetectMapper {

    // 之前的插入方法
    void insertDetection(Detection detection);

    // 新增的搜索方法
    List<Detection> searchDetections(SearchDTO searchDTO);

    List<Detection> selectDates();
}
