package dev.chan.drive.common.errors;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  HttpStatus httpStatus();

  String name();

  String message();
}
