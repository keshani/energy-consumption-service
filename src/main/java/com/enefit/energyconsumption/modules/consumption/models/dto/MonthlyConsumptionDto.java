package com.enefit.energyconsumption.modules.consumption.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;

import lombok.Data;

@Data
public class MonthlyConsumptionDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private YearMonth month;
    private BigDecimal totalConsumptionInKwh;
    private BigDecimal totalCostInCentsWithVat;
    private Currency currencyCode;
}
