package com.enefit.energyconsumption.modules.consumption.controller;

import java.time.Year;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enefit.energyconsumption.config.model.AppUserDetail;
import com.enefit.energyconsumption.modules.consumption.models.MonthlyConsumptionResponse;
import com.enefit.energyconsumption.modules.consumption.service.ConsumptionService;

/**
 * Controller to fetch user energy consumptions
 *
 * @author Keshani
 * @since 2025/05/30
 */
@RestController
@RequestMapping("/api/v1/consumption")
public class ConsumptionsController {

    private ConsumptionService consumptionService;

    public ConsumptionsController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping("/monthlyConsumption")
    public MonthlyConsumptionResponse MonthlyConsumptionResponse(@RequestParam Year year,
            @AuthenticationPrincipal UserDetails user)
    {
        AppUserDetail appUserDetail = (AppUserDetail)user;
        MonthlyConsumptionResponse monthlyConsumptionResponse = new MonthlyConsumptionResponse();
        monthlyConsumptionResponse.setUserMonthlyConsumptions(
                consumptionService.getMonthlyConsumptions(appUserDetail.getUserId(), year));
        return monthlyConsumptionResponse;
    }
}
