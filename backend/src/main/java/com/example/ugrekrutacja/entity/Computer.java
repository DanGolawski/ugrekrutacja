
package com.example.ugrekrutacja.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "computers")
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "accounting_date", nullable = false)
    private LocalDate accountingDate;

    @Column(name = "amount_usd", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountUsd;

    @Column(name = "exchange_rate", nullable = false, precision = 12, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "rate_effective_date", nullable = false)
    private LocalDate rateEffectiveDate;

    @Column(name = "amount_pln", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPln;

    protected Computer() {
        // Wymagany przez JPA.
    }

    public Computer(
            String name,
            LocalDate accountingDate,
            BigDecimal amountUsd,
            BigDecimal exchangeRate,
            LocalDate rateEffectiveDate,
            BigDecimal amountPln
    ) {
        this.name = name;
        this.accountingDate = accountingDate;
        this.amountUsd = amountUsd;
        this.exchangeRate = exchangeRate;
        this.rateEffectiveDate = rateEffectiveDate;
        this.amountPln = amountPln;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getAccountingDate() {
        return accountingDate;
    }

    public BigDecimal getAmountUsd() {
        return amountUsd;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public LocalDate getRateEffectiveDate() {
        return rateEffectiveDate;
    }

    public BigDecimal getAmountPln() {
        return amountPln;
    }
}
