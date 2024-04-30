package com.devtron.ai.util;

import com.devtron.ai.exception.BadRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.devtron.ai.datagenerator.DateUtilMockDataGenerator.EPOCH_SECONDS;
import static com.devtron.ai.util.DateUtil.CURRENT_LOCAL_DATE_TIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilTests {

    @Test
    public void test_convertEpochSecondsToLocalDateTime_when_validEpochSecondsProvided_then_shouldReturnLocalDateTime() {
        LocalDateTime localDateTime = DateUtil.convertEpochSecondsToLocalDateTime(EPOCH_SECONDS);
        assertNotNull(localDateTime);
    }

    @Test(expected = BadRequestException.class)
    public void test_convertLocalDateTimeToLocalDate_when_localDateTimeProvidedIsNull_then_throwBadRequestException() {
        DateUtil.convertLocalDateTimeToLocalDate(null);
    }

    @Test
    public void test_convertLocalDateTimeToLocalDate_when_localDateTimeProvidedIsNotNull_then_returnConvertedLocalDate() {
        final LocalDate localDate = DateUtil.convertLocalDateTimeToLocalDate(CURRENT_LOCAL_DATE_TIME);
        assertEquals(CURRENT_LOCAL_DATE_TIME.toLocalDate(), localDate);
    }

}
