package com.cy.transferapi.repository;

import com.cy.transferapi.model.Transfer;
import org.springframework.data.repository.CrudRepository;

public interface ITransferRepository extends CrudRepository<Transfer, Long> {
}
