package com.jay.recognition.domain.search.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private String driver;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
