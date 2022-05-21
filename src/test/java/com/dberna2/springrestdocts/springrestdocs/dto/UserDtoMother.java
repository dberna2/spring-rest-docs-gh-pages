package com.dberna2.springrestdocts.springrestdocs.dto;

import com.dberna2.springrestdocts.springrestdocs.ConstrainedFields;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.List;

import static com.dberna2.springrestdocts.springrestdocs.ConstrainedFields.buildField;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.restdocs.payload.JsonFieldType.*;

public class UserDtoMother {

  static final ConstrainedFields fields = new ConstrainedFields(UserDto.class);

  public static UserDto create() {
    UserDto userToCreate = new UserDto();
    userToCreate.setName("Andy");
    userToCreate.setLastname("Jackson");
    userToCreate.setAge(40);
    userToCreate.setEmail("email@example.com");
    return userToCreate;
  }

  public static List<FieldDescriptor> buildCreateUserRequestFields() {
    return Arrays.asList(
        buildField("id", NUMBER, "User identifier", FALSE, fields, "-"),
        buildField("name", STRING, "User name", TRUE, fields, "Trinidad"),
        buildField("lastname", STRING, "User lastname", TRUE, fields, "Rivas"),
        buildField("age", NUMBER, "User age", TRUE, fields, "34"),
        buildField("email", STRING, "User email", TRUE, fields, "mail@example.com"),
        buildField("accounts", STRING, "Related user accounts", FALSE, fields, "-"));
  }

  public static List<FieldDescriptor> buildCreateUserResponseFields() {
    return Arrays.asList(
        buildField("id", NUMBER, "User identifier", TRUE, fields),
        buildField("name", STRING, "User name", TRUE, fields),
        buildField("lastname", STRING, "User lastname", TRUE, fields),
        buildField("age", NUMBER, "User age", TRUE, fields),
        buildField("email", STRING, "User email", TRUE, fields),
        buildField("accounts", STRING, "User email", FALSE, fields));
  }

  public static ParameterDescriptor buildGetUserIdentifierPathParameters() {
    Attributes.Attribute attribute = new Attributes.Attribute("exampleValue", "1");
    return fields.withPathParameter("id").description("User identifier").attributes(attribute);
  }

  public static List<FieldDescriptor> buildGetUserResponseFields() {
    return Arrays.asList(
        buildField("id", NUMBER, "User identifier", TRUE, fields),
        buildField("name", STRING, "User name", TRUE, fields),
        buildField("lastname", STRING, "User lastname", TRUE, fields),
        buildField("age", NUMBER, "User age", TRUE, fields),
        buildField("email", STRING, "User email", TRUE, fields),
        buildField("accounts", ARRAY, "Related user accounts", FALSE, fields));
  }

  public static List<FieldDescriptor> buildGetAllUsersResponseFields() {
    return Arrays.asList(
        buildField("[].id", NUMBER, "User identifier", TRUE, fields),
        buildField("[].name", STRING, "User name", TRUE, fields),
        buildField("[].lastname", STRING, "User lastname", TRUE, fields),
        buildField("[].age", NUMBER, "User age", TRUE, fields),
        buildField("[].email", STRING, "User email", TRUE, fields),
        buildField("[].accounts", STRING, "User email", FALSE, fields));
  }
}
