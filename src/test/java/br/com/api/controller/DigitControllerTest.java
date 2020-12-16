package br.com.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import br.com.api.digit.resource.Resource;
import br.com.api.digit.service.DigitService;

class DigitControllerTest {

  private DigitService service;
  private DigitController controller;

  @BeforeEach
  void setUp() {
    this.service = mock(DigitService.class);
    this.controller = new DigitController(this.service);
  }

  @Test
  void convertTest() {
    // given
    String digit = "9875";
    String quantity = "1";
    String username = "alves";
    Resource resource = new Resource(8);

    // when
    BDDMockito.when(this.service.calculete(digit, quantity, username)).thenReturn(resource);

    // then
    ResponseEntity<Resource> responseEntity = this.controller.convert(digit, quantity, username);

    Resource result = responseEntity.getBody();

    assertEquals(resource.getResult(), result.getResult());

    verify(this.service, times(1)).calculete(Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString());
  }

}
