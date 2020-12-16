package br.com.api.digit.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitSender;
import br.com.api.domain.repository.UniqueDigitRepository;

class DigitSenderImplTest {

  private UniqueDigitRepository repository;
  private RestTemplate restTemplate;
  private String authentication;
  private String baseUri;
  private DigitSender service;

  @BeforeEach
  void setUp() {
    this.repository = mock(UniqueDigitRepository.class);
    this.restTemplate = mock(RestTemplate.class);
    this.authentication = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed1";
    this.baseUri = "http://172.17.0.2:8081/api/digit/";

    this.service =
        new DigitSenderImpl(this.repository, this.restTemplate, this.authentication, this.baseUri);
  }

  @Test
  void shouldCalculateTest() {
    // given
    String digit = "9875";
    String quantity = "1";
    String username = "alves";
    Long result = 8L;
    ResponseEntity<Long> response = new ResponseEntity<Long>(result, HttpStatus.OK);

    // when
    BDDMockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
        Mockito.any(), Mockito.<Class<Long>>any())).thenReturn(response);

    // then
    Resource calculete = this.service.calculete(digit, quantity, username);
    verify(this.restTemplate, times(1)).exchange(Mockito.anyString(), Mockito.any(HttpMethod.class),
        Mockito.any(), Mockito.<Class<Long>>any());

    assertEquals(result, calculete.getResult());
  }
}
