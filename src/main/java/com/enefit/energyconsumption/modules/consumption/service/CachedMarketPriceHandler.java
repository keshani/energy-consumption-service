package com.enefit.energyconsumption.modules.consumption.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.enefit.energyconsumption.common.exception.PriceNotAvailableForRangeException;
import com.enefit.energyconsumption.config.MarketPriceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.enefit.energyconsumption.common.util.DateUtil;
import com.enefit.energyconsumption.modules.consumption.models.ElectricityPrice;

@Slf4j
@Service
public class CachedMarketPriceHandler implements MarketPriceHandler {

    private final WebClient webClient;
    private final MarketPriceConfig config;
    private final Map<String, ElectricityPrice> priceCache = new ConcurrentHashMap<>();

    public CachedMarketPriceHandler(final WebClient.Builder webClientBuilder, final MarketPriceConfig config) {
        this.config = config;
        webClient = webClientBuilder
                .baseUrl(this.config.getBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.cachePricesForPastYear();
    }

    /**
     * Retrieves electricity price for a given date. If not cached, fetches from API and caches it.
     */
    @Override
    public ElectricityPrice getPriceForGivenDate(final OffsetDateTime startDate) {
        final OffsetDateTime startDateInUTC = DateUtil.convertToTimeZone(startDate, ZoneOffset.UTC).get();
        return priceCache.computeIfAbsent(startDate.toLocalDate().toString(), key -> fetchMissingPrices(
                startDateInUTC));
    }

    /**
     * Caches electricity prices for the past 12 months during bean initialization.
     */
    private void cachePricesForPastYear() {
        try{
            log.info("Started to fetch the Cache Prices for Past 12 months");
            OffsetDateTime end = OffsetDateTime.now(ZoneOffset.UTC);
            OffsetDateTime start = end.minusMonths(12);

            List<ElectricityPrice> prices = fetchPricesFromExternalApi(start, end);
            prices.stream().forEach(price ->
                    priceCache.put(generateCacheKey(price.getFromDateTime()), price));
            log.info("Successfully added Cache Prices for Past 12 months");
        } catch(Exception ex) {
            log.error("Initial Price retrieval for past 12 months got failed " ,ex);
        }
    }

    private ElectricityPrice fetchMissingPrices(final OffsetDateTime startDate) {
        log.info("Started to fetch prices for date: {}", startDate);
        OffsetDateTime endDate = startDate
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999_999_999);

        endDate = DateUtil.convertToTimeZone(endDate, ZoneOffset.UTC).get();

        List<ElectricityPrice> prices = fetchPricesFromExternalApi(startDate, endDate);
        log.info("Successful fetch prices for date: {}", startDate);
        return prices.getFirst();
    }

    private List<ElectricityPrice> fetchPricesFromExternalApi(final OffsetDateTime startDate, final OffsetDateTime endDate) {
       try {
           List<ElectricityPrice> prices=  webClient.get()
                   .uri(uriBuilder -> uriBuilder
                           .path(this.config.getPriceUrl())
                           .queryParam("startDateTime", startDate)
                           .queryParam("endDateTime", endDate)
                           .build())
                   .retrieve()
                   .bodyToFlux(ElectricityPrice.class)
                   .collectList().block();
           if (prices == null || prices.isEmpty()) {
               throw new PriceNotAvailableForRangeException( startDate, endDate);
           }
           return prices;
       } catch (Exception e) {
           throw new RuntimeException(
                   String.format("Market price API call failed for range %s to %s", startDate, endDate), e);

       }
    }

    private static String generateCacheKey(final OffsetDateTime fromDateTime) {
        return DateUtil.convertToTimeZone(fromDateTime, ZoneOffset.UTC).get().toLocalDate().toString();
    }
}
