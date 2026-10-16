package com.example.ugrekrutacja.client;

import com.example.ugrekrutacja.client.dto.NbpExchangeRateResponse;
import com.example.ugrekrutacja.client.dto.NbpRate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Comparator;

@Component
public class NbpClient {

    private final RestClient restClient;

    public NbpClient(RestClient nbpRestClient) {
        this.restClient = nbpRestClient;
    }

    public NbpRate getUsdRate(LocalDate date) {
        try {
            NbpExchangeRateResponse response = restClient.get()
                    .uri("/exchangerates/rates/A/USD/{date}/?format=json", date)
                    .retrieve()
                    .body(NbpExchangeRateResponse.class);

            return extractLatestRate(response, date);

        } catch (HttpClientErrorException.NotFound ex) {
            // Brak kursu w danym dniu, np. w niedzielę.
            return getLastAvailableRate(date);
        }
    }

    private NbpRate getLastAvailableRate(LocalDate date) {
        LocalDate startDate = date.minusDays(7);

        NbpExchangeRateResponse response = restClient.get()
                .uri("/exchangerates/rates/A/USD/{start}/{end}/?format=json",
                        startDate, date)
                .retrieve()
                .body(NbpExchangeRateResponse.class);

        return extractLatestRate(response, date);
    }

    private NbpRate extractLatestRate(
            NbpExchangeRateResponse response,
            LocalDate requestedDate
    ) {
        if (response == null || response.rates() == null) {
            throw new IllegalStateException("NBP zwrócił pustą odpowiedź");
        }

        return response.rates().stream()
                .filter(rate -> !rate.effectiveDate().isAfter(requestedDate))
                .max(Comparator.comparing(NbpRate::effectiveDate))
                .orElseThrow(() -> new IllegalStateException(
                        "Brak dostępnego kursu USD dla daty " + requestedDate
                ));
    }
}
