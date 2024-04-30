package com.devtron.ai.service;

import com.devtron.ai.dto.request.SearchLogRequestDto;
import com.devtron.ai.dto.response.SearchLogResponseDto;

public interface SearchLogService {

    SearchLogResponseDto searchLogs(SearchLogRequestDto searchLogRequestDto);

}
