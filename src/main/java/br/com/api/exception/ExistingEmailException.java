package br.com.api.exception;

public class ExistingEmailException extends RuntimeException {

  public ExistingEmailException(String message) {
    super(message);
  }
}
