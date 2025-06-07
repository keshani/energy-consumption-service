package com.enefit.energyconsumption.modules.consumption.models;

import java.util.List;
import java.util.Map;

import com.enefit.energyconsumption.modules.consumption.models.dto.ConsumptionDto;

public class MonthlyConsumptionPerMeterResponse {
    private String month;
    private Map<Long, List<ConsumptionDto>> consumptionsPerMeterPoint;
}
