package com.devtron.ai.service.impl;

import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.devtron.ai.dto.request.SearchLogRequestDto;
import com.devtron.ai.dto.response.SearchLogResponseDto;
import com.devtron.ai.exception.BadRequestException;
import com.devtron.ai.service.AwsService;
import com.devtron.ai.service.SearchLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.devtron.ai.constants.CommonConstants.DEFAULT_ZERO;
import static com.devtron.ai.constants.CommonConstants.FORWARD_SLASH;
import static com.devtron.ai.constants.CommonConstants.HOUR_INCREMENT_COUNTER;
import static com.devtron.ai.constants.CommonConstants.ONE_HOUR_IN_MILLIS;
import static com.devtron.ai.constants.CommonConstants.TEXT_FILE_EXTENSION;
import static com.devtron.ai.util.DateUtil.convertEpochSecondsToLocalDateTime;
import static com.devtron.ai.util.DateUtil.convertLocalDateTimeToLocalDate;

@Slf4j
@Service
public class SearchLogServiceImpl implements SearchLogService {

    private final AwsService awsService;

    private final String bucketName;

    public SearchLogServiceImpl(final AwsService awsService,
                                @Value("${amazonproperties.s3.bucket.name}") final String bucketName) {
        this.awsService = awsService;
        this.bucketName = bucketName;
    }

    @Override
    public SearchLogResponseDto searchLogs(final SearchLogRequestDto searchLogRequestDto) {
        if (!validateSearchLogRequestDtoAndSetDefault(searchLogRequestDto)) {
            log.error("Invalid search request body");
            throw new BadRequestException("Invalid search request body");
        }
        final List<String> matchedLines = searchAWSForKeyAndReturnMatchedLines(searchLogRequestDto);
        return prepareSearchResponseDto(matchedLines);
    }

    private Boolean validateSearchLogRequestDtoAndSetDefault(final SearchLogRequestDto searchLogRequestDto) {
        if (Objects.isNull(searchLogRequestDto)) {
            log.warn("SearchLogRequestDto is null");
            return false;
        }
        if (Objects.isNull(searchLogRequestDto.getSearchKeyword())) {
            log.error("searchLogRequestDto.getSearchKeyword() is null");
            return false;
        }
        final long currentTime = System.currentTimeMillis();
        if (Objects.isNull(searchLogRequestDto.getStartTime())) {
            log.warn("searchLogRequestDto.getStartTime() is null, setting it to current time - 1 hour");
            searchLogRequestDto.setStartTime(currentTime - ONE_HOUR_IN_MILLIS);
            return true;
        }
        if (Objects.isNull(searchLogRequestDto.getEndTime())) {
            log.warn("searchLogRequestDto.getEndTime() is null, setting it to current time");
            searchLogRequestDto.setStartTime(currentTime);
            return true;
        }
        return true;
    }

    private SearchLogResponseDto prepareSearchResponseDto(final List<String> matchedLines) {
        final SearchLogResponseDto searchLogResponseDto = new SearchLogResponseDto();
        searchLogResponseDto.setMatchedLogLines(matchedLines);
        searchLogResponseDto.setCount(matchedLines.size());
        return searchLogResponseDto;
    }

    private List<String> searchAWSForKeyAndReturnMatchedLines(final SearchLogRequestDto searchLogRequestDto) {
        final List<String> matchedLines = new ArrayList<>();
        final LocalDateTime startTime = convertEpochSecondsToLocalDateTime(searchLogRequestDto.getStartTime());
        final LocalDateTime endTime = convertEpochSecondsToLocalDateTime(searchLogRequestDto.getEndTime());
        for (LocalDateTime date = startTime; date.isBefore(endTime.plusDays(HOUR_INCREMENT_COUNTER));
             date = date.plusHours(HOUR_INCREMENT_COUNTER)) {
            final ListObjectsV2Request listRequest =
                    awsService.getListObjectsV2RequestForS3(bucketName, generatePrefixForAwsS3Bucket(date));
            ListObjectsV2Result result;
            do {
                result = awsService.fetchObjectsForTheGivenPrefix(bucketName, listRequest);
                processObjectsResultAndSetMatchedLines(matchedLines, result, searchLogRequestDto.getSearchKeyword());
                listRequest.setContinuationToken(result.getNextContinuationToken());
            }
            while (result.isTruncated());
        }
        return matchedLines;
    }

    private String generatePrefixForAwsS3Bucket(final LocalDateTime date) {
        return convertLocalDateTimeToLocalDate(date).toString() + FORWARD_SLASH + getHourForPrefix(date) +
               TEXT_FILE_EXTENSION;
    }

    private String getHourForPrefix(final LocalDateTime date) {
        return date.getHour() < 10 ? DEFAULT_ZERO + date.getHour() : String.valueOf(date.getHour());
    }

    private void processObjectsResultAndSetMatchedLines(final List<String> matchedLines,
                                                        final ListObjectsV2Result result,
                                                        final String searchKeyword) {
        for (final S3ObjectSummary objectSummary: result.getObjectSummaries()) {
            final String key = objectSummary.getKey();
            if (key.endsWith(TEXT_FILE_EXTENSION)) {
                fetchS3ObjectAndCheckForMatchedLines(key, searchKeyword, matchedLines);
            }
        }
    }

    private void fetchS3ObjectAndCheckForMatchedLines(final String key, final String searchKeyword,
                                                      final List<String> matchedLines) {
        try (final S3Object object = awsService.fetchObjectForTheGivenKey(bucketName, key);
             final BufferedReader reader = new BufferedReader(
                     new InputStreamReader(object.getObjectContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchKeyword)) {
                    matchedLines.add(line);
                }
            }
        } catch (final IOException e) {
            log.warn("Error while reading S3 object with key: {}, search keyword: {}", key,
                     searchKeyword, e);
        }
    }
}
