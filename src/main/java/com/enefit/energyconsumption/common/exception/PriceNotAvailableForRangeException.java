package com.enefit.energyconsumption.common.exception;

import java.time.OffsetDateTime;

/**
 * Thrown when electricity price data is not available for the specified date range.
 */
public class PriceNotAvailableForRangeException extends RuntimeException {
    public PriceNotAvailableForRangeException(OffsetDateTime startDate, OffsetDateTime endDate) {
        super(String.format("Prices not available for date range: %s – %s", startDate, endDate));
    }
}

