package com.cy.transferapi.bdd.stepdefs;

import com.cy.transferapi.config.DependencyConfiguration;
import com.cy.transferapi.dto.AccountDto;
import com.cy.transferapi.dto.TransferDto;
import com.cy.transferapi.model.Account;
import com.cy.transferapi.model.Transfer;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.cy.transferapi.testparameters.TestUtils.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@ContextConfiguration(classes =
        {DependencyConfiguration.class}, loader = SpringBootContextLoader.class)
@SpringBootTest
public class StepDefs {

    @Value("${stepdefs.baseUrl}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;
    //cached values
    private Long beneficiaryId, remitterId;
    private HttpHeaders headers = new HttpHeaders();

    @Given("^api populates the beneficiary  and the remitter with  (\\d+) currency")
    public void mutationFile(BigDecimal currencyInitializeAmount) {
        headers.setContentType(MediaType.APPLICATION_JSON);

        final String baseUrlForNewAccounts = baseUrl + "accounts/new";

        AccountDto remitter = buildAccountRequest(VALID_ACCOUNT_NAME_1, currencyInitializeAmount);
        HttpEntity<AccountDto> requestEntity = new HttpEntity<AccountDto>(remitter, headers);

        //create   remitter
        ResponseEntity<Account> response = exchangeAccountRequest(baseUrlForNewAccounts, requestEntity, HttpMethod.PUT);

        remitterId = response.getBody().getId();

        //confirm the response for remitter is 2x
        assertTrue(response.getStatusCode().is2xxSuccessful());

        AccountDto beneficiary = buildAccountRequest(VALID_ACCOUNT_NAME_2, currencyInitializeAmount);

        requestEntity = new HttpEntity<>(beneficiary, headers);

        //create   beneficiary
        response = exchangeAccountRequest(baseUrlForNewAccounts, requestEntity, HttpMethod.PUT);

        beneficiaryId = response.getBody().getId();

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @When("^remitter sends (\\d+) currency to remitter")
    public void variablesFile(BigDecimal transferAmount) {
        final String baseUrlForTransfers = baseUrl + "accounts/transfer";

        //create the transfer object with transferAmount
        TransferDto transferDto = buildTransferRequest(beneficiaryId, remitterId, transferAmount);

        HttpEntity<TransferDto> requestEntity = new HttpEntity<>(transferDto, headers);

        ResponseEntity<Account> response = exchangeAccountRequest(baseUrlForTransfers, requestEntity, HttpMethod.PUT);
        //confirm request is OK
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }


    @Then("remitter has (\\d+) currencies and the beneficiary has (\\d+)")
    public void verifyResponseStatusCode(BigDecimal expectedRemitterBalance, BigDecimal expectedBeneficiaryBalance) throws URISyntaxException {

        final String baseUrlForAllAccounts = baseUrl + "accounts";

        ResponseEntity<Iterable<Account>> response = restTemplate.exchange(
                baseUrlForAllAccounts,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Iterable<Account>>() {
                });

        List<Account> parties = StreamSupport.stream(response.getBody().spliterator(), false)
                .collect(Collectors.toList());

        Account actualRemitter = parties.stream()
                .filter(p -> p.getId() == remitterId)
                .collect(toSingleton()).orElseThrow();

        Account actualBeneficiary = parties.stream()
                .filter(p -> p.getId() == beneficiaryId)
                .collect(toSingleton()).orElseThrow();

        assertEquals(expectedRemitterBalance.compareTo(actualRemitter.getBalance()) ,0);
        assertEquals(expectedBeneficiaryBalance.compareTo(actualBeneficiary.getBalance()) ,0);
    }

    public static <T> Collector<T, ?, Optional<T>> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> list.size() == 1 ? Optional.of(list.get(0)) : Optional.empty()
        );
    }

    private ResponseEntity<Account> exchangeAccountRequest(String url, HttpEntity requestEntity, HttpMethod method) {
        ResponseEntity<Account> response = restTemplate.exchange(url, method,
                requestEntity, Account.class);
        return response;
    }

    private ResponseEntity<Transfer> exchangeTransferRequest(String url, HttpEntity requestEntity, HttpMethod method) {
        ResponseEntity<Transfer> response = restTemplate.exchange(url, method,
                requestEntity, Transfer.class);
        return response;
    }

}
