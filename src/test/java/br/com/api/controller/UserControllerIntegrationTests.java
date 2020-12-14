package br.com.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import br.com.api.constants.UserConstantsForTests;
import br.com.api.provider.RegisterDTOProviderTests;
import br.com.api.user.dto.RegisterDTO;

@SpringBootTest
class UserControllerIntegrationTests implements UserConstantsForTests {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @ParameterizedTest
  @ArgumentsSource(RegisterDTOProviderTests.class)
  void shouldRegisterWithSucess(RegisterDTO registerData) throws Exception {
    this.mockMvc
        .perform(post(PATH_AUTH.concat("register")).content(createJsonRegisterData(registerData))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated()).andExpect(header().exists("Location"));
  }

  private String createJsonRegisterData(RegisterDTO registerData) throws JsonProcessingException {
    return MAPPER.writeValueAsString(registerData);
  }

}
