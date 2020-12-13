package br.com.api.digit.service.impl;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitSender;

@Service
public class DigitSenderImpl implements DigitSender {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${api.authentication}")
  private String authentication;

  @Value("${api.digit}")
  private String baseUri;

  @Cacheable("result")
  public Resource calculete(String digit, String quantity) {
    String uri = this.baseUri.concat(digit).concat("/").concat(quantity).concat("/")
        .concat(this.authentication);
    HttpHeaders httpHeaders = createHttpHeaders();
    HttpEntity<Object> httpEntity = createHttpEntity(httpHeaders);
    System.out.println("fazendo request");
    Long result = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Long.class).getBody();
    return new Resource(result);
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
