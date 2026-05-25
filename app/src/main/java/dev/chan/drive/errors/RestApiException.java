package dev.chan.drive.errors;

import lombok.Getter;

@Getter
public class RestApiException extends RuntimeException {
  private final ErrorCode errorCode;

  public RestApiException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public RestApiException(ErrorCode errorCode) {
    super(errorCode.message());
    this.errorCode = errorCode;
  }
}
