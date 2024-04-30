package com.devtron.ai.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchLogRequestDto {

    private String searchKeyword;

    private Long startTime;

    private Long endTime;

}
