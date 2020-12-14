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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    log.info("Send request to URI [{}]", uri);
    HttpHeaders httpHeaders = createHttpHeaders();
    HttpEntity<Object> httpEntity = createHttpEntity(httpHeaders);
    Long result = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Long.class).getBody();
    log.info("Response: {}", result);
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
