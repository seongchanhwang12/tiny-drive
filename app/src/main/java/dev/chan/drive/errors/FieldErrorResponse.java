package dev.chan.drive.errors;

import org.springframework.validation.FieldError;

public record FieldErrorResponse(String field, String message) {
  public static FieldErrorResponse from(FieldError ex) {
    return new FieldErrorResponse(ex.getField(), ex.getDefaultMessage());
  }
}
