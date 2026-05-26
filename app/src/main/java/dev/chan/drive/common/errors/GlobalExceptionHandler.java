package dev.chan.drive.common.errors;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException ex,
      final HttpHeaders headers,
      final HttpStatusCode status,
      final WebRequest request) {
    log.warn(
        "Request validation failed. fieldCount={}", ex.getBindingResult().getFieldErrorCount());
    return handleErrorResponse(ex, CommonErrorCode.INVALID_PARAM);
  }

  @ExceptionHandler(RestApiException.class)
  protected ResponseEntity<Object> handleApiException(final RestApiException ex) {
    log.warn(
        "Handled API exception. code={}, message={}", ex.getErrorCode().name(), ex.getMessage());
    return handleErrorResponse(ex.getErrorCode());
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleUnexpectedException(final Exception ex) {
    log.error("Unexpected exception occurred", ex);
    return handleErrorResponse(CommonErrorCode.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<Object> handleErrorResponse(final ErrorCode errorCode) {
    return ResponseEntity.status(errorCode.httpStatus()).body(RestApiErrorResponse.from(errorCode));
  }

  private ResponseEntity<Object> handleErrorResponse(
      final BindException ex, final ErrorCode errorCode) {

    List<FieldErrorResponse> errors =
        ex.getFieldErrors().stream().map(FieldErrorResponse::from).toList();

    return ResponseEntity.status(errorCode.httpStatus())
        .body(RestApiErrorResponse.from(errorCode, errors));
  }
}
