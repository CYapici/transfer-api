package com.cy.transferapi.dto;

import java.math.BigDecimal;

public class AccountDto {
    private String name;
    private BigDecimal initialBalance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
