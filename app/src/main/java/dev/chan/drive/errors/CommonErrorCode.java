package dev.chan.drive.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  INVALID_PARAM(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
  ;
  private final HttpStatus httpStatus;
  private final String message;
}
