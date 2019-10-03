package com.cy.transferapi.service;

import com.cy.transferapi.exception.EntityCreationException;
import com.cy.transferapi.exception.TransferServiceException;
import com.cy.transferapi.model.Account;
import com.cy.transferapi.model.Transfer;
import com.cy.transferapi.repository.IAccountRepository;
import com.cy.transferapi.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransferService {

    private IAccountRepository accountRepository;
    private ITransferRepository transferRepository;
 

    @Autowired
    public void setAccountRepository(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setTransferRepository(ITransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Transactional
    public synchronized Account createNewAccount(String name, BigDecimal initialBalance) throws TransferServiceException {
        try {
            Account account = new Account(name, initialBalance);
            return this.accountRepository.save(account);
        } catch (DataAccessException e) {
            throw new TransferServiceException("DataAccess EXCEPTION WITH ERROR MESSAGE: " + e.getMessage(), e);
        } catch (EntityCreationException e) {
            throw new TransferServiceException("Invalid EXCEPTION WITH ERROR MESSAGE: " + e.getMessage(), e);
        }
    }

    @Transactional
    public synchronized Transfer transfer(long sourceAccountId, long destinationAccountId, BigDecimal transferAmount)
            throws TransferServiceException {
        try {

            if (transferAmount.compareTo(BigDecimal.ZERO) < 0) {
                throw new TransferServiceException("INVALID TRANSACTION AMOUNT : PLEASE INSERT A POSITIVE OR ZERO VALUE");
            }

            Account sourceAccount = Optional.of(this.accountRepository.findById(sourceAccountId)).orElseThrow(TransferServiceException::new).get();
            Account destinationAccount = Optional.of(this.accountRepository.findById(destinationAccountId)).orElseThrow(TransferServiceException::new).get();

            if (!sourceAccount.subtract(transferAmount)) {
                throw new TransferServiceException("INSUFFICIENT FUNDS");
            }

            destinationAccount.add(transferAmount);

            this.accountRepository.save(sourceAccount);
            this.accountRepository.save(destinationAccount);

            Transfer transfer = new Transfer(sourceAccount.getId(), destinationAccount.getId(), transferAmount);
            return this.transferRepository.save(transfer);
        } catch (EntityCreationException | NullPointerException e) {
            throw new TransferServiceException(e);
        }
    }

    public Iterable<Account> findAllAccounts() {
        return this.accountRepository.findAll();
    }

    public Iterable<Transfer> findAllTransfers() {
        return this.transferRepository.findAll();
    }
}
