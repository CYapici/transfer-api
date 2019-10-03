package com.cy.transferapi.model;

import org.junit.Test;

import java.math.BigDecimal;

import static com.cy.transferapi.testparameters.TestUtils.*;
import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void equalsShouldBeFunctioningProperly_WhenCompared() {
        Account account = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_1000);
        account.setId(ACCOUNT_IO_1);

        Account accountInstance = this.copyAccount(account);
        accountInstance.setBalance(CURRENCY_1000000);

        Account otherAccount = new Account(VALID_ACCOUNT_NAME_2, CURRENCY_1000);
        otherAccount.setId(ACCOUNT_IO_2);

        Account otherAccountInstance = this.copyAccount(otherAccount);
        otherAccountInstance.setBalance(CURRENCY_1000000);

        assertNotSame(account, accountInstance);
        assertNotSame(otherAccount, otherAccountInstance);

        assertNotEquals(account, otherAccount);
        assertNotEquals(account, otherAccountInstance);

        assertEquals(account, accountInstance);
        assertEquals(otherAccount, otherAccountInstance);
    }

    @Test
    public void hashcodeShouldBeFunctioningProperly_WhenCompared() throws Exception {
        Account accountTestOne = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_1000);
        accountTestOne.setId(ACCOUNT_IO_1);

        Account accountSameTestOne = this.copyAccount(accountTestOne);
        accountSameTestOne.setBalance(CURRENCY_1000000);

        Account accountTestTwo = new Account(VALID_ACCOUNT_NAME_2, CURRENCY_1000);
        accountTestTwo.setId(ACCOUNT_IO_2);

        Account accountSameTestTwo = this.copyAccount(accountTestTwo);
        accountSameTestTwo.setBalance(CURRENCY_1000000);

        assertNotSame(accountTestOne, accountSameTestOne);
        assertNotSame(accountTestTwo, accountSameTestTwo);

        assertNotEquals(accountTestOne.hashCode(), accountTestTwo.hashCode());
        assertNotEquals(accountTestOne.hashCode(), accountSameTestTwo.hashCode());

        assertTrue(accountTestOne.hashCode() == accountSameTestOne.hashCode());
        assertTrue(accountTestTwo.hashCode() == accountSameTestTwo.hashCode());
    }

    @Test
    public void testHasEnoughBalanceForTransfer() throws Exception {
        // have
        Account accountBalanceOneThousand = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_1000);

        assertTrue(accountBalanceOneThousand.hasEnoughBalanceForTransfer(BigDecimal.ZERO));

        assertTrue(accountBalanceOneThousand.hasEnoughBalanceForTransfer(BigDecimal.TEN));

        assertTrue(accountBalanceOneThousand.hasEnoughBalanceForTransfer(CURRENCY_100));

        assertTrue(accountBalanceOneThousand.hasEnoughBalanceForTransfer(CURRENCY_1000));

        assertFalse(accountBalanceOneThousand.hasEnoughBalanceForTransfer(CURRENCY_1000.add(BigDecimal.ONE)));

        Account accountBalanceZero = new Account(VALID_ACCOUNT_NAME_2, BigDecimal.ZERO);

        assertTrue(accountBalanceZero.hasEnoughBalanceForTransfer(BigDecimal.ZERO));

        assertFalse(accountBalanceZero.hasEnoughBalanceForTransfer(BigDecimal.ONE));

        assertFalse(accountBalanceZero.hasEnoughBalanceForTransfer(BigDecimal.TEN));

        assertFalse(accountBalanceZero.hasEnoughBalanceForTransfer(CURRENCY_100));

        assertFalse(accountBalanceZero.hasEnoughBalanceForTransfer(CURRENCY_1000));

        assertFalse(accountBalanceZero.hasEnoughBalanceForTransfer(CURRENCY_1000.add(BigDecimal.ONE)));

        Account accountBalanceOne = new Account(VALID_ACCOUNT_NAME_2, BigDecimal.ONE);

        assertTrue(accountBalanceOne.hasEnoughBalanceForTransfer(BigDecimal.ZERO));

        assertTrue(accountBalanceOne.hasEnoughBalanceForTransfer(BigDecimal.ONE));

        assertFalse(accountBalanceOne.hasEnoughBalanceForTransfer(BigDecimal.TEN));

        assertFalse(accountBalanceOne.hasEnoughBalanceForTransfer(CURRENCY_100));

        assertFalse(accountBalanceOne.hasEnoughBalanceForTransfer(CURRENCY_1000));

        assertFalse(accountBalanceOne.hasEnoughBalanceForTransfer(CURRENCY_1000.add(BigDecimal.ONE)));
    }

    private Account copyAccount(Account other) {
        Account account = new Account(other.getName(), other.getBalance());
        account.setId(other.getId());
        return account;
    }
}
