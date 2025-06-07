package com.enefit.energyconsumption.common.util;

import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.Optional;

public final class DateUtil {

    public static Optional<OffsetDateTime> convertToTimeZone(OffsetDateTime offsetDateTime, ZoneOffset zoneOffset) {
        if (offsetDateTime == null || zoneOffset == null) {
            return Optional.empty();
        }
        OffsetDateTime converted = offsetDateTime.withOffsetSameInstant(zoneOffset);
        return Optional.of(converted);
    }

    public static Optional<OffsetDateTime> getStartOfYear(Year year, ZoneOffset zoneOffset) {
        if (year == null || zoneOffset == null) {
            return Optional.empty();
        }
        OffsetDateTime startDate = OffsetDateTime.of(year.getValue(), 1, 1, 0, 0, 0, 0, zoneOffset);
        return Optional.of(startDate);
    }

    public static Optional<OffsetDateTime> getEndOfYear(Year year, ZoneOffset zoneOffset) {
        if (year == null || zoneOffset == null) {
            return Optional.empty();
        }

        OffsetDateTime endDate = OffsetDateTime.of(year.getValue(), 12, 31, 23, 59, 59, 999_999_999, zoneOffset);
        return Optional.of(endDate);
    }
}
