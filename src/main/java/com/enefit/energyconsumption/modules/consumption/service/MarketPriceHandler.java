package com.enefit.energyconsumption.modules.consumption.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.modules.consumption.models.ElectricityPrice;

@Service
public interface MarketPriceHandler {

    /**
     * Retrieves electricity price for a given date. If not cached, fetches from API and caches it.
     */
    ElectricityPrice getPriceForGivenDate(OffsetDateTime startDate);

}
