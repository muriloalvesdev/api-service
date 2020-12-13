package br.com.api.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface UserConstantsForTests {
  static final ObjectMapper MAPPER = new ObjectMapper();
  static final String PATH_AUTH = "/api/auth/";
  static final String EMAIL = "murilohenrique.ti@outlook.com.br";
  static final String FIRST_NAME = "Murilo";
  static final String LAST_NAME = "Batista";
  static final String PASSWORD = "123456";
  static final String PASSWORD_ENCRYPT =
      "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";
  static final String TOKEN =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
}
