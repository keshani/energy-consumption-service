package com.enefit.energyconsumption.modules.consumption.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.enefit.energyconsumption.common.enums.AmountUnitType;
import com.enefit.energyconsumption.common.util.DateUtil;
import com.enefit.energyconsumption.modules.consumption.models.ElectricityPrice;
import com.enefit.energyconsumption.modules.consumption.models.dto.MonthlyConsumptionDto;
import com.enefit.energyconsumption.modules.consumption.models.entity.Consumption;
import com.enefit.energyconsumption.modules.consumption.repository.ConsumptionInfoRepository;

@Service
@Slf4j
public class ConsumptionServiceImpl
        implements ConsumptionService
{
    private final MarketPriceHandler marketDataHandler;

    private final ConsumptionInfoRepository consumptionInfoRepository;

    private Predicate <ElectricityPrice> isPriceAvilable = (price) -> {
        if(price == null || price.getCentsPerKwhWithVat() == null){
            return false;
        }
        return true;
    };

    public ConsumptionServiceImpl(final MarketPriceHandler marketDataHandler,
            final ConsumptionInfoRepository consumptionInfoRepository)
    {
        this.marketDataHandler = marketDataHandler;
        this.consumptionInfoRepository = consumptionInfoRepository;
    }

    /**
     * Retrieves and calculates monthly electricity consumption and cost for a user in the given year.
     * Prices are retrieved from market data and grouped by month.
     */
    @Override
    public List<MonthlyConsumptionDto> getMonthlyConsumptions(Long userID, Year year) {
        if (userID == null || year == null) {
           throw new IllegalArgumentException("UserID and year are mandatory");
        }

        final OffsetDateTime startDate = DateUtil.getStartOfYear(year, ZoneOffset.UTC).get();
        OffsetDateTime endDate = DateUtil.getEndOfYear(year, ZoneOffset.UTC).get();

        // Skip future dates if querying the current year
        if (endDate.isAfter(OffsetDateTime.now(ZoneOffset.UTC))) {
            endDate = OffsetDateTime.now(ZoneOffset.UTC);
        }

        final List<Consumption> consumptions =
                this.consumptionInfoRepository.findAllByUserIdAndConsumptionDatePeriod(userID, startDate, endDate);

        log.info("Consumptions found: {} for user: {}  for year: {}", consumptions.size(), userID, year);

        final Map<String, MonthlyConsumptionDto> monthlyMap = new HashMap<>();

       try {
           for (Consumption consumption : consumptions) {
               LocalDate consumptionDate = consumption.getConsumptionTime().toLocalDate();
               String monthKey = YearMonth.from(consumptionDate).toString();
               final BigDecimal consumptionAmount = convertConsumptionToKWH(consumption);

               final ElectricityPrice price = marketDataHandler.getPriceForGivenDate(consumption.getConsumptionTime());
               if(!isPriceAvilable.test(price)) {
                   throw new RuntimeException(String.format("Price not available for the given date %s", consumption.getConsumptionTime()));
               }
               final BigDecimal costInCents = getConsumptionPriceInCents(consumption, price);

               // Calculate the monthly consumption
               monthlyMap.compute(monthKey, (key, dto) -> {
                   // if the month is not added to the Map, create new MonthlyConsumptionDto for the given month
                   if (dto == null) {
                       dto = new MonthlyConsumptionDto();
                       dto.setMonth(YearMonth.parse(key));
                       dto.setTotalConsumptionInKwh(consumptionAmount);
                       dto.setTotalCostInCentsWithVat(costInCents);
                       dto.setCurrencyCode(Currency.getInstance("EUR"));
                       return dto;
                   } else {
                       // if the month is already added to the Map, add the given consumption cost to total monthly consumption
                       dto.setTotalConsumptionInKwh(dto.getTotalConsumptionInKwh().add(consumptionAmount));
                       dto.setTotalCostInCentsWithVat(dto.getTotalCostInCentsWithVat().add(costInCents));
                       return dto;
                   }
               });
           }
       } catch(Exception e) {
           log.error("Monthly consumption calculation failed", e);
           throw new RuntimeException("Monthly consumption calculation failed", e);
       }

        final List<MonthlyConsumptionDto> result = new ArrayList<>(monthlyMap.values());
        result.sort(Comparator.comparing(MonthlyConsumptionDto::getMonth));
        return result;
    }


    private BigDecimal getConsumptionPriceInCents(Consumption consumption, ElectricityPrice price) {
        BigDecimal amount = convertConsumptionToKWH(consumption);
        BigDecimal totalCost = amount.multiply(price.getCentsPerKwhWithVat());
      return totalCost.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal convertConsumptionToKWH(Consumption consumption) {
        BigDecimal convertedConsumption = consumption.getAmount();
        if(AmountUnitType.MWh == consumption.getAmountUnit()) {
            convertedConsumption = consumption.getAmount().multiply(BigDecimal.valueOf(1000));
        }
        return convertedConsumption;
    }
}
