package br.com.api.digit.service;

import br.com.api.digit.resource.Resource;

public interface DigitSender {
  Resource calculete(String digit, String quantity);
}
