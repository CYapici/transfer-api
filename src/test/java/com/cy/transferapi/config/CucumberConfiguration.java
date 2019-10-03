package com.cy.transferapi.config;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber", "json:target/Destination/cucumber.json"},
        features = {"src/test/resources/features"},
        glue = {"com/cy/transferapi/bdd/stepdefs"},
        tags = {"@CurrencyTransfer"})
@RunWith(Cucumber.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CucumberConfiguration {

}