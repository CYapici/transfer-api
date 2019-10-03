package com.cy.transferapi.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.cy.transferapi.testparameters.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TransferTest {
    @Mock
    private Account remitter;

    @Mock
    private Account beneficiary;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        when(this.remitter.getId()).thenReturn(ACCOUNT_IO_1);
        when(this.remitter.getName()).thenReturn(VALID_ACCOUNT_NAME_1);
        when(this.beneficiary.getId()).thenReturn(ACCOUNT_IO_2);
        when(this.beneficiary.getName()).thenReturn(VALID_ACCOUNT_NAME_2);
    }

    @Test
    public void equalsShouldBeFunctioningProperly_WhenCompared() {
        Transfer transferOne = new Transfer(this.remitter.getId(), this.beneficiary.getId(), CURRENCY_1000);
        transferOne.setId(ACCOUNT_IO_1);

        Transfer transferOneInstance = copyTransfer(transferOne);

        Transfer otherTransfer = new Transfer(this.remitter.getId(), this.beneficiary.getId(), CURRENCY_1000);
        otherTransfer.setId(ACCOUNT_IO_2);

        Transfer otherTransferInstance = copyTransfer(otherTransfer);

        assertNotSame(transferOne, transferOneInstance);
        assertNotSame(otherTransfer, otherTransferInstance);

        assertNotEquals(transferOne.hashCode(), otherTransfer.hashCode());
        assertNotEquals(transferOne.hashCode(), otherTransferInstance.hashCode());

        assertTrue(transferOne.hashCode() == transferOneInstance.hashCode());
        assertTrue(otherTransfer.hashCode() == otherTransferInstance.hashCode());
    }

    @Test
    public void hashcodeShouldBeFunctioningProperly_WhenCompared() {
        Transfer transfer = new Transfer(this.remitter.getId(), this.beneficiary.getId(), CURRENCY_1000);
        transfer.setId(ACCOUNT_IO_1);
        Transfer transferOneInstance = copyTransfer(transfer);
        Transfer otherTransfer = new Transfer(this.remitter.getId(), this.beneficiary.getId(), CURRENCY_1000);
        otherTransfer.setId(ACCOUNT_IO_2);
        Transfer transferSameTestTwo = copyTransfer(otherTransfer);

        assertNotSame(transfer, transferOneInstance);
        assertNotSame(otherTransfer, transferSameTestTwo);

        assertNotEquals(transfer.hashCode(), otherTransfer.hashCode());
        assertNotEquals(transfer.hashCode(), transferSameTestTwo.hashCode());

        assertTrue(transfer.hashCode() == transferOneInstance.hashCode());
        assertTrue(otherTransfer.hashCode() == transferSameTestTwo.hashCode());
    }

    private Transfer copyTransfer(Transfer other) {
        Transfer transfer = new Transfer(other.getSourceAccountId(), other.getDestinationAccountId(), other.getAmount());
        transfer.setId(other.getId());
        return transfer;
    }
}
