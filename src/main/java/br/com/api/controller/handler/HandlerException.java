package br.com.api.controller.handler;

import javax.management.relation.RoleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import br.com.api.controller.handler.resource.ApiException;
import br.com.api.exception.DigitInvalidException;
import br.com.api.exception.EmailNotFoundException;
import br.com.api.exception.ExistingEmailException;
import br.com.api.exception.IllegalRoleException;

@ControllerAdvice
public class HandlerException extends ResponseEntityExceptionHandler {

  private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
  private static final HttpStatus CONFLICT = HttpStatus.CONFLICT;
  private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

  @ExceptionHandler(EmailNotFoundException.class)
  public ResponseEntity<ApiException> handleEmailNotFoundException(EmailNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(createResponse(ex.getMessage(), NOT_FOUND.value()));
  }

  @ExceptionHandler(DigitInvalidException.class)
  public ResponseEntity<ApiException> handleDigitInvalidException(DigitInvalidException ex) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(createResponse(ex.getMessage(), BAD_REQUEST.value()));
  }

  @ExceptionHandler(ExistingEmailException.class)
  public ResponseEntity<ApiException> handleExistingEmailException(ExistingEmailException ex) {
    return ResponseEntity.status(CONFLICT).body(createResponse(ex.getMessage(), CONFLICT.value()));
  }

  @ExceptionHandler(IllegalRoleException.class)
  public ResponseEntity<ApiException> handleIllegalRoleException(IllegalRoleException ex) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(createResponse(ex.getMessage(), BAD_REQUEST.value()));
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<ApiException> handleRoleNotFoundException(RoleNotFoundException ex) {
    return ResponseEntity.status(NOT_FOUND)
        .body(createResponse(ex.getMessage(), NOT_FOUND.value()));
  }

  private ApiException createResponse(String message, int httpValue) {
    return new ApiException(message, httpValue);
  }
}
