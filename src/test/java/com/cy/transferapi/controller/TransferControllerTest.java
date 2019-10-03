package com.cy.transferapi.controller;

import com.cy.transferapi.dto.AccountDto;
import com.cy.transferapi.dto.TransferDto;
import com.cy.transferapi.repository.IAccountRepository;
import com.cy.transferapi.repository.ITransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static com.cy.transferapi.testparameters.TestUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransferControllerTest {

    private static final String NEW_ACCOUNT_CONTEXT = "/accounts/new/";
    private static final String ROOT_CONTEXT = "/accounts/";
    private static final String TRANSFERS_CONTEXT = "/transfers/";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private ITransferRepository transferRepository;


    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.transferRepository.deleteAll();
        this.accountRepository.deleteAll();
    }

    public String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = NestedServletException.class)
    public void shouldThrowNestedServletException_whenInvokedWithEmptyName() throws Exception {
        AccountDto accountDto = buildAccountRequest("", INVALID_AMOUNT);
        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
        ;
    }

    @Test(expected = NestedServletException.class)
    public void shouldThrowNestedServletException_whenInvokedWithNegatedBalance() throws Exception {
        AccountDto accountDto = buildAccountRequest(VALID_ACCOUNT_NAME_1, CURRENCY_100.negate());
        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
        ;
    }

    @Test(expected = NestedServletException.class)
    public void shouldThrowNestedServletException_whenInvokedWithDuplicateAccountNAMEs() throws Exception {
        AccountDto accountDto = buildAccountRequest(VALID_ACCOUNT_NAME_1, CURRENCY_100);
        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        ;
        accountDto = buildAccountRequest(VALID_ACCOUNT_NAME_1, CURRENCY_1000);
        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
        ;
    }

    @Test
    public void testAddTransfer() throws Exception {
        AccountDto accountDto = buildAccountRequest(VALID_ACCOUNT_NAME_1, CURRENCY_100);
        AccountDto accountDto2 = buildAccountRequest(VALID_ACCOUNT_NAME_2, CURRENCY_100);

        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto2))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.get(TRANSFERS_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(0)))
        ;

        TransferDto transferDto = buildTransferRequest(ACCOUNT_IO_2, ACCOUNT_IO_1, CURRENCY_10);


        this.mockMvc.perform(MockMvcRequestBuilders.put("/accounts/transfer/")
                .content(asJsonString(transferDto))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        this.mockMvc.perform(MockMvcRequestBuilders.get(TRANSFERS_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].sourceAccountId", is(1)))
                .andExpect(jsonPath("$[0].destinationAccountId", is(2)))
                .andExpect(jsonPath("$[0].amount", is(10.0)))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.get(ROOT_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(VALID_ACCOUNT_NAME_1)))
                .andExpect(jsonPath("$[0].balance", is(90.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is(VALID_ACCOUNT_NAME_2)))
                .andExpect(jsonPath("$[1].balance", is(110.0)))
        ;
    }

    @Test
    public void shouldAddCountWorkProperly_when() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(ROOT_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(0)))
        ;
        AccountDto accountDto = buildAccountRequest(VALID_ACCOUNT_NAME_1, CURRENCY_100);
        AccountDto accountDto2 = buildAccountRequest(VALID_ACCOUNT_NAME_2, CURRENCY_50);
        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(VALID_ACCOUNT_NAME_1)))
                .andExpect(jsonPath("$.balance", is(100)))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.get(ROOT_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(VALID_ACCOUNT_NAME_1)))
                .andExpect(jsonPath("$[0].balance", is(100.0)))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.put(NEW_ACCOUNT_CONTEXT)
                .content(asJsonString(accountDto2))
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is(VALID_ACCOUNT_NAME_2)))
                .andExpect(jsonPath("$.balance", is(50)))
        ;

        this.mockMvc.perform(MockMvcRequestBuilders.get(ROOT_CONTEXT)
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(VALID_ACCOUNT_NAME_1)))
                .andExpect(jsonPath("$[0].balance", is(100.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is(VALID_ACCOUNT_NAME_2)))
                .andExpect(jsonPath("$[1].balance", is(50.0)))
        ;
    }

}
