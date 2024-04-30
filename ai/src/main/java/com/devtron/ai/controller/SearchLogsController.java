package com.devtron.ai.controller;

import com.devtron.ai.dto.request.SearchLogRequestDto;
import com.devtron.ai.service.SearchLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/search")
public class SearchLogsController {

    private final SearchLogService searchLogService;

    @Autowired
    public SearchLogsController(final SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }

    @PostMapping(value = "/logs")
    public ResponseEntity<?> searchLogs(@RequestBody final SearchLogRequestDto searchLogRequestDto) {
        return new ResponseEntity<>(searchLogService.searchLogs(searchLogRequestDto), HttpStatus.OK);
    }

}
