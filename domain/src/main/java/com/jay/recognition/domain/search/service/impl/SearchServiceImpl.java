package com.jay.recognition.domain.search.service.impl;

import com.jay.recognition.domain.detect.dao.Detection;
import com.jay.recognition.domain.detect.mapper.DetectMapper;

import static com.jay.recognition.domain.detect.service.impl.DetectHelper.helmet;

import com.jay.recognition.domain.detect.service.impl.DetectServiceImpl;
import com.jay.recognition.domain.search.dto.SearchDTO;
import com.jay.recognition.domain.search.service.SearchService;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    DetectMapper detectMapper;
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    public Map<String, Object> search(SearchDTO searchDTO) {
        List<Detection> detections = detectMapper.searchDetections(searchDTO);
        logger.info("search param :" + searchDTO);
        logger.info("search Result :" + detections);
        return transform(detections);
    }

    public Map<String, Object> transform(List<Detection> detections) {
        List<String> labels = new ArrayList<>();
        List<Integer> helmetValues = new ArrayList<>();
        List<Integer> noHelmetValues = new ArrayList<>();

        List<String> pieLabels = new ArrayList<>();
        List<Integer> pieValues = new ArrayList<>();

        int helmet = 0;
        int noHelmet = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Integer> time2Index = new HashMap<>();
        for (Detection detection : detections) {
            String time = sdf.format(detection.getDetectTime());
            if (time2Index.containsKey(time)) {
                Integer index = time2Index.get(time);
                Integer helmetValue = helmetValues.get(index);
                helmetValue += !helmet(detection.getStatus()) ? 1 : 0;
                helmetValues.set(index, helmetValue);

                Integer noHelmetValue = noHelmetValues.get(index);
                noHelmetValue += helmet(detection.getStatus()) ? 1 : 0;
                noHelmetValues.set(index, noHelmetValue);
            } else {
                time2Index.put(time, labels.size());
                labels.add(time);
                helmetValues.add(!helmet(detection.getStatus()) ? 1 : 0);
                noHelmetValues.add(helmet(detection.getStatus()) ? 1 : 0);
            }
            if (helmet(detection.getStatus())) helmet++;
            else noHelmet++;
        }

        pieLabels.add("戴头盔");
        pieLabels.add("未戴头盔");
        pieValues.add(helmet);
        pieValues.add(noHelmet);
        HashMap<String, Object> map = new HashMap<>();
        map.put("labels", labels);
        map.put("helmetValues", helmetValues);
        map.put("noHelmetValues", noHelmetValues);
        map.put("pieLabels", pieLabels);
        map.put("pieValues", pieValues);
        logger.info("map" + map);
        return map;
    }
}
