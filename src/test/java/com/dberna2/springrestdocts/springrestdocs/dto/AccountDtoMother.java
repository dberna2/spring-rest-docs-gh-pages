package com.dberna2.springrestdocts.springrestdocs.dto;

import com.dberna2.springrestdocts.springrestdocs.ConstrainedFields;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;

import java.util.Arrays;
import java.util.List;

import static com.dberna2.springrestdocts.springrestdocs.ConstrainedFields.buildField;
import static com.dberna2.springrestdocts.springrestdocs.dto.AccountType.CREDIT;
import static com.dberna2.springrestdocts.springrestdocs.dto.AccountType.DEBIT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;

public class AccountDtoMother {

  private static final ConstrainedFields fields = new ConstrainedFields(AccountDto.class);

  public static AccountDto create() {
    AccountDto accountToCreate = new AccountDto();
    accountToCreate.setNumber("4909914807265711");
    accountToCreate.setType(DEBIT);
    return accountToCreate;
  }

  public static ParameterDescriptor buildGetUserIdentifierPathParameters() {
    Attributes.Attribute attribute = new Attributes.Attribute("exampleValue", "1");
    return fields.withPathParameter("id").description("User identifier").attributes(attribute);
  }

  public static List<FieldDescriptor> buildGetUserAccountsResponseFields() {
    return Arrays.asList(
        buildField("[].id", NUMBER, "Account identifier", TRUE, fields),
        buildField("[].type", STRING, "Account type", TRUE, fields),
        buildField("[].number", STRING, "Account number", TRUE, fields),
        buildField("[].balance", NUMBER, "Account balance", FALSE, fields));
  }

  public static List<FieldDescriptor> buildCreateUserAccountsRequestFields() {
    return Arrays.asList(
        buildField("id", STRING, "Account type", FALSE, fields, "-"),
        buildField("type", STRING, "Account type", TRUE, fields, DEBIT.name(), CREDIT.name()),
        buildField("number", STRING, "Account number", TRUE, fields, "4909914807265711"),
        buildField("balance", NUMBER, "Account balance", FALSE, fields, "$8,933.95"));
  }

  public static List<FieldDescriptor> buildCreateUserAccountsResponseFields() {
    return Arrays.asList(
        buildField("id", NUMBER, "Account identifier", TRUE, fields),
        buildField("type", STRING, "Account type", TRUE, fields),
        buildField("number", STRING, "Account number", TRUE, fields),
        buildField("balance", NUMBER, "Account balance", FALSE, fields));
  }
}
