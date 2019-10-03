package com.cy.transferapi.testparameters;

import com.cy.transferapi.dto.AccountDto;
import com.cy.transferapi.dto.TransferDto;

import java.math.BigDecimal;

public class TestUtils {

    public static final BigDecimal INVALID_AMOUNT = new BigDecimal("-1");
    public static final BigDecimal CURRENCY_10 = new BigDecimal("10");
    public static final BigDecimal CURRENCY_50 = new BigDecimal("50");
    public static final BigDecimal CURRENCY_100 = new BigDecimal("100");
    public static final BigDecimal CURRENCY_1000 = new BigDecimal("1000");
    public static final BigDecimal CURRENCY_1000000 = new BigDecimal("1000000");

    public static final String VALID_ACCOUNT_NAME_1 = "SILLY";
    public static final String VALID_ACCOUNT_NAME_2 = "BILLY";

    public static final Long ACCOUNT_IO_1 = 1L;
    public static final Long ACCOUNT_IO_2 = 2L;

    public static final BigDecimal ANY_TRANSFER_AMOUNT = new BigDecimal(1);

    public static AccountDto buildAccountRequest(String name, BigDecimal arrear) {
        AccountDto accountDto = new AccountDto();
        accountDto.setInitialBalance(arrear);
        accountDto.setName(name);
        return accountDto;
    }

    public static TransferDto buildTransferRequest(Long beneficiary, Long remitter, BigDecimal arrear) {
        TransferDto transferDto = new TransferDto();
        transferDto.setSourceId(remitter);
        transferDto.setDestId(beneficiary);
        transferDto.setAmount(arrear);
        return transferDto;
    }

}

