package br.com.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import br.com.api.user.dto.LoginDTO;
import br.com.api.user.dto.RegisterDTO;
import br.com.api.user.model.AccessToken;

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
  void shouldCrudUser(RegisterDTO registerData) throws Exception {
    LoginDTO dto = new LoginDTO();
    dto.setEmail(registerData.getEmail());
    dto.setPassword(registerData.getPassword());

    this.mockMvc
        .perform(post(PATH_AUTH.concat("register")).content(createJsonRegisterData(registerData))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated()).andExpect(header().exists("Location"));

    String token = this.mockMvc
        .perform(post(PATH_AUTH.concat("login")).content(createJsonLogin(dto))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn().getResponse().getContentAsString();
    AccessToken accessToken = MAPPER.readValue(token, AccessToken.class);

    this.mockMvc
        .perform(get(PATH_AUTH.concat("find?email=").concat(EMAIL))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken.getAccessToken()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].name", is(FIRST_NAME.toUpperCase())))
        .andExpect(jsonPath("$.[0].lastName", is(LAST_NAME.toUpperCase())))
        .andExpect(jsonPath("$.[0].email", is(EMAIL)));

    this.mockMvc
        .perform(get(PATH_AUTH.concat("find")).contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken.getAccessToken()))
        .andExpect(status().isOk());

    this.mockMvc
        .perform(put(PATH_AUTH.concat("update")).content(createJsonRegisterData(registerData))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken.getAccessToken()))
        .andExpect(status().isOk());

    this.mockMvc
        .perform(delete(PATH_AUTH.concat("delete?email=").concat(EMAIL))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header("Authorization", "Bearer " + accessToken.getAccessToken()))
        .andExpect(status().isOk());
  }

  private String createJsonRegisterData(RegisterDTO registerData) throws JsonProcessingException {
    return MAPPER.writeValueAsString(registerData);
  }

  private String createJsonLogin(LoginDTO loginDTO) throws JsonProcessingException {
    return MAPPER.writeValueAsString(loginDTO);
  }

}
