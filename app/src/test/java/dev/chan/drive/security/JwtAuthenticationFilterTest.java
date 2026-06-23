package dev.chan.drive.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.drive.app.auth.JwtPrincipal;
import dev.chan.drive.app.auth.LoginUseCase;
import dev.chan.drive.config.JwtTokenProvider;
import dev.chan.drive.config.MySqlTestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@AutoConfigureMockMvc
@Import(MySqlTestcontainersConfig.class)
class JwtAuthenticationFilterTest {

  @Autowired private JwtTokenProvider jwtTokenProvider;
  @Autowired ObjectMapper objectMapper;

  /** 테스트용 API */
  @TestConfiguration
  static class TestControllerConfig {
    @RestController
    static class ProtectedTestController {

      /* security 로부터 보호되는 api */
      @GetMapping("/api/test/protected")
      String protectedApi() {
        return "ok";
      }
    }
  }

  @Autowired MockMvc mockMvc;

  @Test
  void 토큰이_없으면_401을_반환한다() throws Exception {
    mockMvc
        .perform(get("/api/test/protected"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
  }

  @Test
  void 잘못된_토큰이면_401을_반환한다() throws Exception {
    mockMvc
        .perform(get("/api/test/protected").header("Authorization", "Bearer invalid-token"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
  }

  @Test
  void 유효한_토큰이면_요청한_API_접근할_수_있다() throws Exception {
    String token = jwtTokenProvider.issue(new JwtPrincipal(1L));

    mockMvc
        .perform(get("/api/test/protected").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void Bearer_접두사가_없으면_401을_반환한다() throws Exception {
    mockMvc
        .perform(get("/api/test/protected").header("Authorization", "invalid-token"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("INVALID_TOKEN"));
  }

  /**
   * 로그인 API가 permitAll 설정에 의해
   *
   * <p>JWT 없이 접근 가능한지 검증한다.
   *
   * <p>로그인 성공 여부는 검증하지 않는다.
   */
  @Test
  void 공개_api는_토큰_없이_접근할_수_있다() throws Exception {
    LoginUseCase.Input request = new LoginUseCase.Input("test@test.com", "Password1!");

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
  }

  /*
  @Autowired FilterChainProxy filterChainProxy;
  @Test
  void 필터_순서_확인() {

    filterChainProxy
        .getFilterChains()
        .forEach(
            chain -> {
              chain.getFilters().forEach(filter -> System.out.println(filter.getClass().getName()));
            });
  }
  */
}
