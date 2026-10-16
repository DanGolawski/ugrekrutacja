
package com.example.ugrekrutacja.service;

import com.example.ugrekrutacja.client.NbpClient;
import com.example.ugrekrutacja.client.dto.NbpRate;
import com.example.ugrekrutacja.client.dto.ConversionRequest;
import com.example.ugrekrutacja.client.dto.ConversionResult;
import com.example.ugrekrutacja.entity.Computer;
import com.example.ugrekrutacja.repository.ComputerRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private final NbpClient nbpClient;
    private final ComputerRepository computerRepository;

    public CurrencyConversionService(
            NbpClient nbpClient,
            ComputerRepository computerRepository
    ) {
        this.nbpClient = nbpClient;
        this.computerRepository = computerRepository;
    }

    public List<ConversionResult> convert(
            List<ConversionRequest> requests
    ) {
        Map<LocalDate, NbpRate> ratesByDate = new HashMap<>();
        List<ConversionResult> results = new ArrayList<>();

        for (ConversionRequest request : requests) {
            NbpRate rate = ratesByDate.computeIfAbsent(
                    request.accountingDate(),
                    nbpClient::getUsdRate
            );

            BigDecimal amountPln = request.amountUsd()
                    .multiply(rate.mid())
                    .setScale(2, RoundingMode.HALF_UP);

            results.add(new ConversionResult(
                    request.name(),
                    request.accountingDate(),
                    request.amountUsd(),
                    rate.mid(),
                    rate.effectiveDate(),
                    amountPln
            ));
        }

        return results;
    }

    
    public List<Computer> convertAndSave(
            List<ConversionRequest> requests
    ) {
        List<ConversionResult> results = convert(requests);

        List<Computer> computers = results.stream()
                .map(result -> new Computer(
                        result.name(),
                        result.accountingDate(),
                        result.amountUsd(),
                        result.exchangeRate(),
                        result.rateEffectiveDate(),
                        result.amountPln()
                ))
                .toList();

        return computerRepository.saveAll(computers);
    }
}
