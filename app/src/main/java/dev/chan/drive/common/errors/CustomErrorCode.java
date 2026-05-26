package dev.chan.drive.common.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

  /* User */
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Duplicate email address."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password format."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
