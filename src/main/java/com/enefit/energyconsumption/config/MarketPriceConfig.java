package com.enefit.energyconsumption.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "market-price")
@Data
public class MarketPriceConfig {

    private String baseUrl;
    private String priceUrl;
}

