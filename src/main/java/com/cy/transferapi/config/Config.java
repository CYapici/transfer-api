package com.cy.transferapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"com.cy.transferapi.model"})
@EnableJpaRepositories(basePackages = {"com.cy.transferapi.repository"})
@EnableTransactionManagement //note that has performance issues
public class Config {
}
