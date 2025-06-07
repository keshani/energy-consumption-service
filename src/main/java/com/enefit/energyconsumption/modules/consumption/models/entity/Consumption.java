package com.enefit.energyconsumption.modules.consumption.models.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.enefit.energyconsumption.common.enums.AmountUnitType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CONSUMPTION")
public class Consumption {

    @Id
    @GeneratedValue
    @Column(name = "CONSUMPTION_ID")
    private Long consumptionId;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "AMOUNT_UNIT", nullable = false)
    @Enumerated(EnumType.STRING)
    private AmountUnitType amountUnit;

    @Column(name = "CONSUMPTION_TIME", nullable = false)
    private OffsetDateTime consumptionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "METERING_POINT_ID", nullable = false)
    private MeteringPoint meteringPoint;
}
