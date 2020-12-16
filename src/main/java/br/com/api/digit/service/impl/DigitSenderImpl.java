package br.com.api.digit.service.impl;

import java.util.Arrays;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitSender;
import br.com.api.domain.model.UniqueDigit;
import br.com.api.domain.repository.UniqueDigitRepository;
import br.com.api.exception.DigitInvalidException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
class DigitSenderImpl implements DigitSender {

  private UniqueDigitRepository repository;
  private RestTemplate restTemplate;
  private String authentication;
  private String baseUri;

  DigitSenderImpl(UniqueDigitRepository repository, RestTemplate restTemplate,
      @Value("${api.authentication}") String authentication,
      @Value("${api.digit}") String baseUri) {
    this.repository = repository;
    this.restTemplate = restTemplate;
    this.authentication = authentication;
    this.baseUri = baseUri;
  }

  public Resource calculete(String digit, String quantity, String username) {
    if (!checkNumber(Double.valueOf(digit))) {
      throw new DigitInvalidException(digit);
    }
    Long result = this.sendRequest(digit, quantity);
    this.persist(digit, quantity, username.toUpperCase(), result);
    log.info("Response: {}", result);
    return new Resource(result);
  }

  private void persist(String digit, String quantity, String username, Long result) {
    UniqueDigit uniqueDigit = new UniqueDigit(UUID.randomUUID(), username, digit, quantity, result);
    if (!this.repository.existsUsername(username)) {
      this.repository.save(uniqueDigit);
    }
  }

  private Long sendRequest(String digit, String quantity) {
    String uri = this.baseUri.concat(digit).concat("/").concat(quantity).concat("/")
        .concat(this.authentication);
    log.info("Send request to URI [{}]", uri);
    HttpHeaders httpHeaders = createHttpHeaders();
    HttpEntity<Object> httpEntity = createHttpEntity(httpHeaders);
    Long result = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Long.class).getBody();
    return result;
  }

  private static boolean checkNumber(double num) {
    int digit = (int) num;
    return (((double) digit) == num);
  }

  private HttpHeaders createHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    return headers;
  }

  private HttpEntity<Object> createHttpEntity(HttpHeaders headers) {
    return new HttpEntity<Object>(headers);
  }
}
