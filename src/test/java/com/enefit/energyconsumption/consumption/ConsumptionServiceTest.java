package com.enefit.energyconsumption.consumption;

import com.enefit.energyconsumption.common.enums.AmountUnitType;
import com.enefit.energyconsumption.modules.consumption.models.ElectricityPrice;
import com.enefit.energyconsumption.modules.consumption.models.dto.MonthlyConsumptionDto;
import com.enefit.energyconsumption.modules.consumption.models.entity.Consumption;
import com.enefit.energyconsumption.modules.consumption.repository.ConsumptionInfoRepository;
import com.enefit.energyconsumption.modules.consumption.service.ConsumptionServiceImpl;
import com.enefit.energyconsumption.modules.consumption.service.MarketPriceHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ConsumptionServiceTest {

    @Mock
    private ConsumptionInfoRepository consumptionInfoRepository;

    @Mock
    private MarketPriceHandler marketDataHandler;

    @InjectMocks
    private ConsumptionServiceImpl consumptionService;

    Long userId = 1L;

    @Test
    void consumptionServiceTest_shouldReturnMonthlyConsumptionsForPastYear() {

        List<Consumption> consumptions = getTestConsumptionList();

        Mockito.when(consumptionInfoRepository
                        .findAllByUserIdAndConsumptionDatePeriod(
                                ArgumentMatchers.eq(userId),
                                any(OffsetDateTime.class),
                                any(OffsetDateTime.class)))
                .thenReturn(consumptions);
        // Mock market price
        ElectricityPrice price = new ElectricityPrice();
        price.setCentsPerKwhWithVat(new BigDecimal("4.157353"));
        price.setEurPerMwhWithVat(new BigDecimal("41.57353"));

        Mockito.when(marketDataHandler.getPriceForGivenDate(any()))
                .thenReturn(price);

        // Act
        List<MonthlyConsumptionDto> result = consumptionService.getMonthlyConsumptions(userId, Year.now());

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(YearMonth.from(consumptions.get(5).getConsumptionTime()), result.get(0).getMonth());
        assertEquals(YearMonth.from(consumptions.get(3).getConsumptionTime()), result.get(1).getMonth());
        assertEquals(YearMonth.from(consumptions.get(0).getConsumptionTime()), result.get(2).getMonth());
        assertEquals(new BigDecimal("1234.0056"), result.get(0).getTotalConsumptionInKwh());
        assertEquals(new BigDecimal("5130.20"), result.get(0).getTotalCostInCentsWithVat());
        assertEquals(new BigDecimal("1350.567"), result.get(1).getTotalConsumptionInKwh());
        assertEquals(new BigDecimal("5614.78"), result.get(1).getTotalCostInCentsWithVat());
        assertEquals(new BigDecimal("1442.4567"), result.get(2).getTotalConsumptionInKwh());
        assertEquals(new BigDecimal("5996.80"), result.get(2).getTotalCostInCentsWithVat());
        assertEquals(Currency.getInstance("EUR"), result.get(2).getCurrencyCode());
    }

    @Test
    void shouldThrowExceptionForNullUserOrYear() {
        assertThrows(IllegalArgumentException.class, () -> consumptionService.getMonthlyConsumptions(null, Year.now()));
        assertThrows(IllegalArgumentException.class, () -> consumptionService.getMonthlyConsumptions(1L, null));
    }



    private List<Consumption> getTestConsumptionList() {

        // Arrange
        Year year = Year.now();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        // Fake consumption data
        Consumption consumption1 = new Consumption();
        consumption1.setConsumptionTime(now.minusMonths(1));
        consumption1.setAmount(new BigDecimal("200"));
        consumption1.setAmountUnit(AmountUnitType.kWh);

        Consumption consumption2 = new Consumption();
        consumption2.setConsumptionTime(now.minusMonths(1));
        consumption2.setAmount(new BigDecimal("12.4567"));
        consumption2.setAmountUnit(AmountUnitType.kWh);

        Consumption consumption3 = new Consumption();
        consumption3.setConsumptionTime(now.minusMonths(1));
        consumption3.setAmount(new BigDecimal("1.23"));
        consumption3.setAmountUnit(AmountUnitType.MWh);

        Consumption consumption4 = new Consumption();
        consumption4.setConsumptionTime(now.minusMonths(2));
        consumption4.setAmount(new BigDecimal("120.567"));
        consumption4.setAmountUnit(AmountUnitType.kWh);

        Consumption consumption5 = new Consumption();
        consumption5.setConsumptionTime(now.minusMonths(2));
        consumption5.setAmount(new BigDecimal("1.23"));
        consumption5.setAmountUnit(AmountUnitType.MWh);

        Consumption consumption6 = new Consumption();
        consumption6.setConsumptionTime(now.minusMonths(3));
        consumption6.setAmount(new BigDecimal("1234.0056"));
        consumption6.setAmountUnit(AmountUnitType.kWh);

        return List.of(consumption1, consumption2, consumption3, consumption4,consumption5,consumption6);
    }
}
