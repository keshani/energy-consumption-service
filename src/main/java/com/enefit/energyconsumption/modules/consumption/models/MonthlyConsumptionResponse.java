package com.enefit.energyconsumption.modules.consumption.models;

import java.util.List;

import com.enefit.energyconsumption.modules.consumption.models.dto.MonthlyConsumptionDto;

import lombok.Data;

@Data
public class MonthlyConsumptionResponse {
    private List<MonthlyConsumptionDto> userMonthlyConsumptions;
}
