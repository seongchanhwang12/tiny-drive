package dev.chan.drive.error;

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
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token."),
  INVALID_PARAM(HttpStatus.BAD_REQUEST, "Invalid param."),
  ROOT_NOT_FOUND(HttpStatus.NOT_FOUND, "Root folder not found."),
  PARENT_NOT_FOLDER(HttpStatus.BAD_REQUEST, "Parent item must be a folder."),
  DRIVE_NOT_FOUND(HttpStatus.NOT_FOUND, "Drive not found."),
  FILE_SIZE_LIMIT_EXCEEDED(
      HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the maximum allowed limit.");

  private final HttpStatus httpStatus;
  private final String message;
}
