package com.dberna2.springrestdocts.springrestdocs.controller;

import com.dberna2.springrestdocts.springrestdocs.dto.AccountDto;
import com.dberna2.springrestdocts.springrestdocs.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.dberna2.springrestdocts.springrestdocs.dto.AccountType.CREDIT;
import static com.dberna2.springrestdocts.springrestdocs.dto.AccountType.DEBIT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.UriConfigurer.DEFAULT_PORT;
import static org.springframework.restdocs.mockmvc.UriConfigurer.DEFAULT_SCHEME;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserControllerTest {

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
    void createUser() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(UserDto.class);

        UserDto userToCreate = new UserDto();
        userToCreate.setName("Andy");
        userToCreate.setLastname("Jackson");
        userToCreate.setAge(40);
        userToCreate.setEmail("email@example.com");

        this.mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userToCreate))
                ).andDo(document("create-user",
                        requestFields(this.buildCreateUserRequestFields(constrainedFields)),
                        responseFields(this.buildCreateUserResponseFields(constrainedFields))
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
    void createUserAccount() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(AccountDto.class);

        AccountDto accountToCreate = new AccountDto();
        accountToCreate.setNumber("4909914807265711");
        accountToCreate.setType(DEBIT);

        this.mockMvc.perform(post("/users/{id}/accounts", 1)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountToCreate))
                ).andDo(document("create-user-account",
                        requestFields(this.buildCreateUserAccountsRequestFields(constrainedFields)),
                        responseFields(this.buildCreateUserAccountsResponseFields(constrainedFields))
                ))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("$.number").value(accountToCreate.getNumber()))
                .andExpect(jsonPath("$.type").value(accountToCreate.getType().name()));
    }

    @Test
    void getUserById() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(UserDto.class);

        this.mockMvc.perform(get("/users/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("user-by-id",
                        pathParameters(this.buildGetUserIdentifierPathParameters(constrainedFields)),
                        responseFields(this.buildGetUserResponseFields(constrainedFields)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.lastname").isNotEmpty())
                .andExpect(jsonPath("$.age").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test
    void getUserAccounts() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(AccountDto.class);

        this.mockMvc.perform(get("/users/{id}/accounts", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("get-accounts-by-id",
                        pathParameters(this.buildGetUserIdentifierPathParameters(constrainedFields)),
                        responseFields(this.buildGetUserAccountsResponseFields(constrainedFields)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").isNotEmpty())
                .andExpect(jsonPath("$.[0].number").isNotEmpty());
    }

    @Test
    void getAllUsers() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(UserDto.class);

        this.mockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andDo(document("list-users",
                        responseFields(this.buildGetAllUsersResponseFields(constrainedFields)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].name").isNotEmpty())
                .andExpect(jsonPath("$.[0].lastname").isNotEmpty())
                .andExpect(jsonPath("$.[0].age").isNotEmpty())
                .andExpect(jsonPath("$.[0].email").isNotEmpty());
    }

    @Test
    void deleteUserById() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(AccountDto.class);

        this.mockMvc.perform(delete("/users/{id}",1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("delete-user",
                        pathParameters(this.buildGetUserIdentifierPathParameters(constrainedFields)))
                )
                .andExpect(status().isAccepted());
    }

    private List<FieldDescriptor> buildCreateUserRequestFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("id", NUMBER,"User identifier", FALSE, fields, "-"),
                buildField("name", STRING,"User name", TRUE, fields, "Trinidad"),
                buildField("lastname", STRING,"User lastname", TRUE, fields, "Rivas"),
                buildField("age", NUMBER,"User age", TRUE, fields, "34"),
                buildField("email", STRING,"User email", TRUE, fields, "mail@example.com"),
                buildField("accounts", STRING,"Related user accounts", FALSE, fields, "-")
        );
    }

    private List<FieldDescriptor> buildCreateUserResponseFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("id", NUMBER,"User identifier", TRUE, fields),
                buildField("name", STRING,"User name", TRUE, fields),
                buildField("lastname", STRING,"User lastname", TRUE, fields),
                buildField("age", NUMBER,"User age", TRUE, fields),
                buildField("email", STRING,"User email", TRUE, fields),
                buildField("accounts", STRING,"User email", FALSE, fields)
        );
    }

    private List<FieldDescriptor> buildGetUserResponseFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("id", NUMBER,"User identifier", TRUE, fields),
                buildField("name", STRING,"User name", TRUE, fields),
                buildField("lastname", STRING,"User lastname", TRUE, fields),
                buildField("age", NUMBER,"User age", TRUE, fields),
                buildField("email", STRING,"User email", TRUE, fields),
                buildField("accounts", ARRAY, "Related user accounts", FALSE, fields)
        );
    }

    private ParameterDescriptor buildGetUserIdentifierPathParameters(ConstrainedFields fields) {
        Attributes.Attribute attribute = new Attributes.Attribute("exampleValue", "1");
        return fields.withPathParameter("id").description("User identifier").attributes(attribute);
    }

    private List<FieldDescriptor> buildGetUserAccountsResponseFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("[].id", NUMBER,"Account identifier", TRUE, fields),
                buildField("[].type", STRING,"Account type", TRUE, fields),
                buildField("[].number", STRING,"Account number", TRUE, fields),
                buildField("[].balance", NUMBER,"Account balance", FALSE, fields)
        );
    }

    private List<FieldDescriptor> buildCreateUserAccountsRequestFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("id", STRING,"Account type", FALSE, fields, "-"),
                buildField("type", STRING,"Account type", TRUE, fields, DEBIT.name(), CREDIT.name()),
                buildField("number", STRING,"Account number", TRUE, fields, "4909914807265711"),
                buildField("balance", NUMBER,"Account balance", FALSE, fields,"$8,933.95")
        );
    }

    private List<FieldDescriptor> buildCreateUserAccountsResponseFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("id", NUMBER,"Account identifier", TRUE, fields),
                buildField("type", STRING,"Account type", TRUE, fields),
                buildField("number", STRING,"Account number", TRUE, fields),
                buildField("balance", NUMBER,"Account balance", FALSE, fields)
        );
    }

    private List<FieldDescriptor> buildGetAllUsersResponseFields(ConstrainedFields fields) {
        return Arrays.asList(
                buildField("[].id", NUMBER,"User identifier", TRUE, fields),
                buildField("[].name", STRING,"User name", TRUE, fields),
                buildField("[].lastname", STRING,"User lastname", TRUE, fields),
                buildField("[].age", NUMBER,"User age", TRUE, fields),
                buildField("[].email", STRING,"User email", TRUE, fields),
                buildField("[].accounts", STRING,"User email", FALSE, fields)
        );
    }

    private FieldDescriptor buildField(
            String name,
            JsonFieldType type,
            String description,
            boolean isMandatory,
            ConstrainedFields fields,
            String... exampleValues
    ) {
        String exampleValue = String.join(", ", exampleValues);
        Attributes.Attribute attribute = new Attributes.Attribute("exampleValue", exampleValue);
        FieldDescriptor fieldDescriptor = fields
                .withPath(name)
                .type(type)
                .description(description)
                .attributes(attribute);

        if (!isMandatory) {
            fieldDescriptor.optional();
        }
        return fieldDescriptor;
    }

    static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        public ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        public FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), "- ")));
            }

            public ParameterDescriptor withPathParameter(String path) {
            return parameterWithName(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
            }

            public ParameterDescriptor withRequestParameter(String path) {
                return parameterWithName(path).attributes(key("constraints").value(StringUtils
                        .collectionToDelimitedString(this.constraintDescriptions
                                .descriptionsForProperty(path), ". ")));
        }
    }
}