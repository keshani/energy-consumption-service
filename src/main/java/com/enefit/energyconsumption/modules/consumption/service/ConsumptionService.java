package com.enefit.energyconsumption.modules.consumption.service;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.modules.consumption.models.dto.MonthlyConsumptionDto;

@Service
public interface ConsumptionService {
    /**
     * Retrieves and calculates monthly electricity consumption and cost for a user in the given year.
     * Prices are retrieved from market data and grouped by month.
     *
     * @param userID The user's unique identifier.
     * @param year The year to calculate consumption for.
     * @return List of monthly consumption summaries.
     */
    List<MonthlyConsumptionDto> getMonthlyConsumptions(Long userId, Year year);
}
