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
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    void createUser() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(UserDto.class);

        List<FieldDescriptor> requestFieldDescriptor = this.requestCreateUserFieldDescriptor(constrainedFields);
        List<FieldDescriptor> responseFieldDescriptor = this.responseCreateUserFieldDescriptor(constrainedFields);

        UserDto user = new UserDto();
        user.setName("David");
        user.setLastname("Bernal");
        user.setAge(31);
        user.setEmail("leo.bernal1946@gmail.com");

        this.mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andDo(
                        document("create-user",
                                requestFields(requestFieldDescriptor),
                                responseFields(responseFieldDescriptor)
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.lastname").value(user.getLastname()))
                .andExpect(jsonPath("$.age").value(user.getAge()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void getUserById() throws Exception {

        ConstrainedFields constrainedFields = new ConstrainedFields(UserDto.class);

        ParameterDescriptor userPathParameterDescriptor =
                this.userPathParameterDescriptor(constrainedFields);

        List<FieldDescriptor> responseGetUserFieldDescriptor =
                this.responseGetUserByIdFieldDescriptor(constrainedFields);

        this.mockMvc.perform(get("/users/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("get-by-id",
                        pathParameters(userPathParameterDescriptor),
                        responseFields(responseGetUserFieldDescriptor))
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

        ParameterDescriptor userPathParameterDescriptor =
                this.userPathParameterDescriptor(constrainedFields);

        List<FieldDescriptor> userAccountsResponseFieldDescriptor =
                this.getUserAccountsResponseFieldDescriptor(constrainedFields);

        this.mockMvc.perform(get("/users/{id}/accounts", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(document("get-accounts-by-id",
                        pathParameters(userPathParameterDescriptor),
                        responseFields(userAccountsResponseFieldDescriptor))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.[0].type").isNotEmpty())
                .andExpect(jsonPath("$.[0].number").isNotEmpty());
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void deleteUserById() {
    }

    private List<FieldDescriptor> requestCreateUserFieldDescriptor(ConstrainedFields fields) {
        Attributes.Attribute p = new Attributes.Attribute("exampleValue", "");

        return Arrays.asList(
                fields.withPath("name").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("lastname").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("age").type(Integer.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("email").type(String.class.getSimpleName()).description("User name").attributes(p));
    }

    private List<FieldDescriptor> responseCreateUserFieldDescriptor(ConstrainedFields fields) {
        Attributes.Attribute p = new Attributes.Attribute("exampleValue", "34");
        return Arrays.asList(
                fields.withPath("id").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("name").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("lastname").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("age").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("email").type(String.class.getSimpleName()).description("User name").attributes(p));
    }

    private List<FieldDescriptor> responseGetUserByIdFieldDescriptor(ConstrainedFields fields) {
        Attributes.Attribute p = new Attributes.Attribute("exampleValue", "34");
        return Arrays.asList(
                fields.withPath("id").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("name").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("lastname").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("age").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("email").type(String.class.getSimpleName()).description("User name").attributes(p));
    }

    private ParameterDescriptor userPathParameterDescriptor(ConstrainedFields fields) {
        Attributes.Attribute p = new Attributes.Attribute("exampleValue", "34");
        return fields.withPathParameter("id").description("User name").attributes(p);
    }

    private List<FieldDescriptor> getUserAccountsResponseFieldDescriptor(ConstrainedFields fields) {
        Attributes.Attribute p = new Attributes.Attribute("exampleValue", "34");
        return Arrays.asList(
                fields.withPath("[].id").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("[].type").type(String.class.getSimpleName()).description("User name").attributes(p),
                fields.withPath("[].number").type(String.class.getSimpleName()).description("User name").attributes(p));
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