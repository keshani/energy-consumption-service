package com.enefit.energyconsumption.modules.consumption.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Currency;

import lombok.Data;

@Data
public class ConsumptionDto
        implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long consumptionId;
    private Long meteringPointId;
    private BigDecimal amount;
    private String amountUnit;
    private OffsetDateTime consumptionTime;
    private BigDecimal totalCost;
    private Currency currencyCode;
}
