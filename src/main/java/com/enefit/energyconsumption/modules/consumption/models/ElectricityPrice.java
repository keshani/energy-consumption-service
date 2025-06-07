package com.enefit.energyconsumption.modules.consumption.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class ElectricityPrice {
    private BigDecimal centsPerKwh;
    private BigDecimal centsPerKwhWithVat;
    private BigDecimal eurPerMwh;
    private BigDecimal eurPerMwhWithVat;
    private OffsetDateTime fromDateTime;
    private OffsetDateTime toDateTime;
}
