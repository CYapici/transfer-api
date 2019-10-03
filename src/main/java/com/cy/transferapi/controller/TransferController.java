package com.cy.transferapi.controller;

import com.cy.transferapi.dto.AccountDto;
import com.cy.transferapi.dto.TransferDto;
import com.cy.transferapi.model.Account;
import com.cy.transferapi.model.Transfer;
import com.cy.transferapi.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping(value = "/accounts")
    public Iterable<Account> accounts() {
        return this.transferService.findAllAccounts();
    }

    @GetMapping(value = "/transfers")
    public Iterable<Transfer> transfers() {
        return this.transferService.findAllTransfers();
    }

    @PutMapping(value = "/accounts/new")
    public Account newAccount(@RequestBody @Valid AccountDto request) {
        return this.transferService.createNewAccount(request.getName(), request.getInitialBalance());
    }

    @PutMapping(value = "/accounts/transfer")
    public Transfer newAccount(@RequestBody @Valid TransferDto request) {
        return this.transferService.transfer(request.getSourceId(), request.getDestId(), request.getAmount());
    }
}
