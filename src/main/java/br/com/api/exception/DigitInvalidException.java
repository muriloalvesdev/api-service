package br.com.api.exception;

public class DigitInvalidException extends RuntimeException {

  public DigitInvalidException(String digit) {
    super(String.format("Digit [%] must be an integer", digit));
  }

}
