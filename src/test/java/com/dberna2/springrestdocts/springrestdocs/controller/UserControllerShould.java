package com.dberna2.springrestdocts.springrestdocs.controller;

import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.AccountDtoMother;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDtoMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.dberna2.springrestdocts.springrestdocs.dto.AccountType.DEBIT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.UriConfigurer.DEFAULT_PORT;
import static org.springframework.restdocs.mockmvc.UriConfigurer.DEFAULT_SCHEME;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserControllerShould {

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation
    ) {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme(DEFAULT_SCHEME)
                    .withHost("example.com")
                    .withPort(DEFAULT_PORT)
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint())
            ).build();
    }

    @Test
    @DisplayName("Create new user and return http status code 201")
    void createNewUserAndReturnHttpStatusCode201() throws Exception {
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Andy");
        userToCreate.setLastname("Jackson");
        userToCreate.setAge(40);
        userToCreate.setEmail("email@example.com");

        this.mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userToCreate))
                ).andDo(document("create-user",
                        requestFields(UserDtoMother.buildCreateUserRequestFields()),
                        responseFields(UserDtoMother.buildCreateUserResponseFields())
                ))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(userToCreate.getName()))
                .andExpect(jsonPath("$.lastname").value(userToCreate.getLastname()))
                .andExpect(jsonPath("$.age").value(userToCreate.getAge()))
                .andExpect(jsonPath("$.email").value(userToCreate.getEmail()));
    }

    @Test
    @DisplayName("Create new user account by user identifier and return http status code 201")
    void createNewUserAccountByUserIdentifierAndReturnHttpStatusCode201() throws Exception {
        AccountDto accountToCreate = new AccountDto();
        accountToCreate.setNumber("4909914807265711");
        accountToCreate.setType(DEBIT);

        this.mockMvc.perform(post("/users/{id}/accounts", 1)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountToCreate))
                ).andDo(document("create-user-account",
                        requestFields(AccountDtoMother.buildCreateUserAccountsRequestFields()),
                        responseFields(AccountDtoMother.buildCreateUserAccountsResponseFields())
                ))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("$.number").value(accountToCreate.getNumber()))
                .andExpect(jsonPath("$.type").value(accountToCreate.getType().name()));
    }

    @Test
    @DisplayName("Retrieve basic user information by identifier")
    void retrieveBasicUserInformationByIdentifier() throws Exception {
        this.mockMvc.perform(get("/users/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("user-by-id",
                        pathParameters(UserDtoMother.buildGetUserIdentifierPathParameters()),
                        responseFields(UserDtoMother.buildGetUserResponseFields()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.lastname").isNotEmpty())
                .andExpect(jsonPath("$.age").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test
    @DisplayName("Retrieve all user accounts by user identifier")
    void retrieveAllUserAccountsByUserIdentifier() throws Exception {
        this.mockMvc.perform(get("/users/{id}/accounts", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("get-accounts-by-id",
                        pathParameters(AccountDtoMother.buildGetUserIdentifierPathParameters()),
                        responseFields(AccountDtoMother.buildGetUserAccountsResponseFields()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").isNotEmpty())
                .andExpect(jsonPath("$.[0].number").isNotEmpty());
    }

    @Test
    @DisplayName("Retrieve basic information from all registered users")
    void retrieveBasicInformationFromAllRegisteredUsers() throws Exception {
        this.mockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andDo(document("list-users",
                        responseFields(UserDtoMother.buildGetAllUsersResponseFields()))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].name").isNotEmpty())
                .andExpect(jsonPath("$.[0].lastname").isNotEmpty())
                .andExpect(jsonPath("$.[0].age").isNotEmpty())
                .andExpect(jsonPath("$.[0].email").isNotEmpty());
    }

    @Test
    @DisplayName("Delete an existing user by identifier")
    void deleteAnExistingUserByIdentifier() throws Exception {
        this.mockMvc.perform(delete("/users/{id}",1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("delete-user",
                        pathParameters(AccountDtoMother.buildGetUserIdentifierPathParameters()))
                )
                .andExpect(status().isAccepted());
    }
}