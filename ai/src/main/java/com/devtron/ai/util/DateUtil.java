package com.devtron.ai.util;

import com.devtron.ai.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.ZoneOffset.UTC;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    public static final Integer ONE_SECOND_IN_MILLISECONDS = 1000;

    public static final LocalDateTime CURRENT_LOCAL_DATE_TIME = LocalDateTime.now(UTC);

    public static LocalDateTime convertEpochSecondsToLocalDateTime(final long epochSeconds) {
        return LocalDateTime.ofEpochSecond(epochSeconds / ONE_SECOND_IN_MILLISECONDS, 0, UTC);
    }

    public static LocalDate convertLocalDateTimeToLocalDate(final LocalDateTime localDateTime) {
        if(Objects.isNull(localDateTime)) {
            log.error("LocalDateTime is null");
            throw new BadRequestException("Invalid localDateTime provided");
        }
        return localDateTime.toLocalDate();
    }

}
