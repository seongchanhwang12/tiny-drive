package dev.chan.drive.error;

import java.util.List;

public record RestApiErrorResponse(String code, String message, List<FieldErrorResponse> errors) {
  public static RestApiErrorResponse from(ErrorCode errorCode) {
    return new RestApiErrorResponse(errorCode.name(), errorCode.message(), List.of());
  }

  public static RestApiErrorResponse from(ErrorCode errorCode, List<FieldErrorResponse> errors) {
    return new RestApiErrorResponse(errorCode.name(), errorCode.message(), errors);
  }
}
