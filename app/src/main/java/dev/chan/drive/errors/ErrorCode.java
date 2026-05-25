package dev.chan.drive.errors;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  HttpStatus httpStatus();

  String name();

  String message();
}
