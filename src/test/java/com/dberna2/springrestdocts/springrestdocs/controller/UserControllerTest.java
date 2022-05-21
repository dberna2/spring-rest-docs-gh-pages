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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
class UserControllerTest {

  public static final String BASE_PATH = "/users";

  @Autowired private ObjectMapper mapper;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp(
      WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider restDocumentation) {

    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(
                documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme(DEFAULT_SCHEME)
                    .withHost("example.com")
                    .withPort(DEFAULT_PORT)
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()))
            .build();
  }

  @Test
  @DisplayName("should create new user and return http status code 201")
  void shouldCreateNewUserAndReturnHttpStatusCode201() throws Exception {

    UserDto userToCreate = UserDtoMother.create();

    this.mockMvc
        .perform(
            post(BASE_PATH)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(userToCreate)))
        .andDo(
            document(
                "create-user",
                requestFields(UserDtoMother.buildCreateUserRequestFields()),
                responseFields(UserDtoMother.buildCreateUserResponseFields())))
        .andExpect(status().isCreated())
        .andExpect(header().exists(LOCATION))
        .andExpect(jsonPath("id").isNotEmpty())
        .andExpect(jsonPath("$.name").value(userToCreate.getName()))
        .andExpect(jsonPath("$.lastname").value(userToCreate.getLastname()))
        .andExpect(jsonPath("$.age").value(userToCreate.getAge()))
        .andExpect(jsonPath("$.email").value(userToCreate.getEmail()));
  }

  @Test
  @DisplayName("Should create new user account by user identifier and return http status code 201")
  void shouldCreateNewUserAccountByUserIdentifierAndReturnHttpStatusCode201() throws Exception {

    AccountDto accountToCreate = AccountDtoMother.create();

    this.mockMvc
        .perform(
            post(BASE_PATH.concat("/{id}/accounts"), 1)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(accountToCreate)))
        .andDo(
            document(
                "create-user-account",
                requestFields(AccountDtoMother.buildCreateUserAccountsRequestFields()),
                responseFields(AccountDtoMother.buildCreateUserAccountsResponseFields())))
        .andExpect(status().isCreated())
        .andExpect(header().exists(LOCATION))
        .andExpect(jsonPath("id").isNotEmpty())
        .andExpect(jsonPath("$.number").value(accountToCreate.getNumber()))
        .andExpect(jsonPath("$.type").value(accountToCreate.getType().name()));
  }

  @Test
  @DisplayName("should retrieve basic user information by identifier")
  void shouldRetrieveBasicUserInformationByIdentifier() throws Exception {

    this.mockMvc
        .perform(get(BASE_PATH.concat("/{id}"), 1).contentType(APPLICATION_JSON))
        .andDo(
            document(
                "user-by-id",
                pathParameters(UserDtoMother.buildGetUserIdentifierPathParameters()),
                responseFields(UserDtoMother.buildGetUserResponseFields())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").isNotEmpty())
        .andExpect(jsonPath("$.lastname").isNotEmpty())
        .andExpect(jsonPath("$.age").isNotEmpty())
        .andExpect(jsonPath("$.email").isNotEmpty());
  }

  @Test
  @DisplayName("should retrieve all user accounts by user identifier")
  void shouldRetrieveAllUserAccountsByUserIdentifier() throws Exception {

    this.mockMvc
        .perform(get(BASE_PATH.concat("/{id}/accounts"), 1).contentType(APPLICATION_JSON))
        .andDo(
            document(
                "get-accounts-by-id",
                pathParameters(AccountDtoMother.buildGetUserIdentifierPathParameters()),
                responseFields(AccountDtoMother.buildGetUserAccountsResponseFields())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").isNotEmpty())
        .andExpect(jsonPath("$.[0].type").isNotEmpty())
        .andExpect(jsonPath("$.[0].number").isNotEmpty());
  }

  @Test
  @DisplayName("Should retrieve basic information from all registered users")
  void shouldRetrieveBasicInformationFromAllRegisteredUsers() throws Exception {

    this.mockMvc
        .perform(get(BASE_PATH).contentType(APPLICATION_JSON))
        .andDo(
            document("list-users", responseFields(UserDtoMother.buildGetAllUsersResponseFields())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").isNotEmpty())
        .andExpect(jsonPath("$.[0].name").isNotEmpty())
        .andExpect(jsonPath("$.[0].lastname").isNotEmpty())
        .andExpect(jsonPath("$.[0].age").isNotEmpty())
        .andExpect(jsonPath("$.[0].email").isNotEmpty());
  }

  @Test
  @Transactional
  @DisplayName("should delete an existing user by identifier")
  void shouldDeleteAnExistingUserByIdentifier() throws Exception {

    this.mockMvc
        .perform(delete(BASE_PATH.concat("/{id}"), 1).contentType(APPLICATION_JSON))
        .andDo(
            document(
                "delete-user",
                pathParameters(AccountDtoMother.buildGetUserIdentifierPathParameters())))
        .andExpect(status().isAccepted());
  }
}
