package dev.chan.drive.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  HttpStatus httpStatus();

  String name();

  String message();
}
