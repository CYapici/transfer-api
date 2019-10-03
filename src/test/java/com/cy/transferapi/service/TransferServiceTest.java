package com.cy.transferapi.service;

import com.cy.transferapi.exception.EntityCreationException;
import com.cy.transferapi.exception.TransferServiceException;
import com.cy.transferapi.model.Account;
import com.cy.transferapi.model.Transfer;
import com.cy.transferapi.repository.IAccountRepository;
import com.cy.transferapi.repository.ITransferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.cy.transferapi.testparameters.TestUtils.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService productService;
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private ITransferRepository transferRepository;

    @Test(expected = EntityCreationException.class)
    public void shouldThrowEntityCreationException_whenTransferInvoked() {
        Account remitter = new Account(VALID_ACCOUNT_NAME_1, INVALID_AMOUNT);
        remitter.setId(ACCOUNT_IO_1);
        Account beneficiery = new Account(VALID_ACCOUNT_NAME_2, CURRENCY_50);
        beneficiery.setId(ACCOUNT_IO_2);

        new Transfer(ACCOUNT_IO_1, ACCOUNT_IO_2, ANY_TRANSFER_AMOUNT);

        Transfer resultTransfer = productService.transfer(
                ACCOUNT_IO_1, ACCOUNT_IO_2, ANY_TRANSFER_AMOUNT
        );

        assertThat(resultTransfer.getSourceAccountId()).isEqualTo(remitter.getId());
        assertThat(resultTransfer.getDestinationAccountId()).isEqualTo(beneficiery.getId());
    }

    @Test(expected = TransferServiceException.class)
    public void shouldThrowTransferServiceExceptionWithNullRemitterBeneficiary_whenTransferInvoked() {
        Account remitter = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_10);
        remitter.setId(ACCOUNT_IO_1);
        Account beneficiery = new Account(VALID_ACCOUNT_NAME_2, CURRENCY_50);
        beneficiery.setId(ACCOUNT_IO_2);

        productService.transfer(
                ACCOUNT_IO_1, ACCOUNT_IO_2, INVALID_AMOUNT
        );
    }

    @Test
    public void shouldCreateNewAccount_WhenCreateNewAccountInvoked() {

        Account product = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_10);

        doReturn(product).when(accountRepository).save(product);

        Account createdAccount = productService.createNewAccount(VALID_ACCOUNT_NAME_1, CURRENCY_10);

        assertThat(product).isEqualTo(createdAccount);
    }

    @Test
    public void shouldGetAllRecords_WhenFindAllAccountsInvoked() {
        Account product = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_10);

        List<Account> expectedProducts = Arrays.asList(product);

        doReturn(expectedProducts).when(accountRepository).findAll();

        Iterable<Account> allAccountsFromService = productService.findAllAccounts();

        List<Account> actualProducts =
                StreamSupport.stream(allAccountsFromService.spliterator(), false)
                        .collect(Collectors.toList());

        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    public void shuldConfirmRemitterAndBeneficiary_whenTransferInvoked() {

        Account remitter = new Account(VALID_ACCOUNT_NAME_1, CURRENCY_10);
        remitter.setId(ACCOUNT_IO_1);
        Account beneficiery = new Account(VALID_ACCOUNT_NAME_2, CURRENCY_50);
        beneficiery.setId(ACCOUNT_IO_2);

        Transfer transfer = new Transfer(ACCOUNT_IO_1, ACCOUNT_IO_2, ANY_TRANSFER_AMOUNT);

        doReturn(Optional.of(remitter)).when(accountRepository).findById(ACCOUNT_IO_1);
        doReturn(Optional.of(beneficiery)).when(accountRepository).findById(ACCOUNT_IO_2);
        doReturn(transfer).when(transferRepository).save(transfer);

        Transfer resultTransfer = productService.transfer(
                ACCOUNT_IO_1, ACCOUNT_IO_2, ANY_TRANSFER_AMOUNT
        );

        assertThat(resultTransfer.getSourceAccountId()).isEqualTo(remitter.getId());
        assertThat(resultTransfer.getDestinationAccountId()).isEqualTo(beneficiery.getId());
    }

    @Test
    public void shouldGetAllTransfers_WhenFindAllTransfersInvoked() {
        Transfer transfer = new Transfer(ACCOUNT_IO_1, ACCOUNT_IO_2, ANY_TRANSFER_AMOUNT);

        List<Transfer> expectedTransferList = Arrays.asList(transfer);

        doReturn(expectedTransferList).when(transferRepository).findAll();

        Iterable<Transfer> allAccountsFromService = productService.findAllTransfers();

        List<Transfer> actualProducts =
                StreamSupport.stream(allAccountsFromService.spliterator(), false)
                        .collect(Collectors.toList());

        assertThat(actualProducts).isEqualTo(expectedTransferList);
    }


}
