package com.dberna2.springrestdocts.springrestdocs;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

public final class ConstrainedFields {

  public static final String DELIMITER = ", ";
  public static final String EXAMPLE_KEY_VALUE = "exampleValue";

  private final ConstraintDescriptions constraintDescriptions;

  public ConstrainedFields(Class<?> input) {
    this.constraintDescriptions = new ConstraintDescriptions(input);
  }

  public FieldDescriptor withPath(String path) {
    return fieldWithPath(path)
        .attributes(
            key("constraints")
                .value(
                    collectionToDelimitedString(
                        this.constraintDescriptions.descriptionsForProperty(path), "- ")));
  }

  public ParameterDescriptor withPathParameter(String path) {
    return parameterWithName(path)
        .attributes(
            key("constraints")
                .value(
                    collectionToDelimitedString(
                        this.constraintDescriptions.descriptionsForProperty(path), ". ")));
  }

  public ParameterDescriptor withRequestParameter(String path) {
    return parameterWithName(path)
        .attributes(
            key("constraints")
                .value(
                    collectionToDelimitedString(
                        this.constraintDescriptions.descriptionsForProperty(path), ". ")));
  }

  public static FieldDescriptor buildField(
      String name,
      JsonFieldType type,
      String description,
      boolean isMandatory,
      ConstrainedFields fields,
      String... exampleValues) {
    String exampleValue = String.join(DELIMITER, exampleValues);
    Attributes.Attribute attribute = new Attributes.Attribute(EXAMPLE_KEY_VALUE, exampleValue);
    FieldDescriptor fieldDescriptor =
        fields.withPath(name).type(type).description(description).attributes(attribute);

    if (!isMandatory) {
      return fieldDescriptor.optional();
    }
    return fieldDescriptor;
  }
}
