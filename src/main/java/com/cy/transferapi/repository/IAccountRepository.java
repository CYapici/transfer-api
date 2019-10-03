package com.cy.transferapi.repository;

import com.cy.transferapi.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Long> {
}
