package dev.chan.drive.app.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.GlobalExceptionHandler;
import dev.chan.drive.error.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  private MockMvc mockMvc;

  @InjectMocks private AuthController authController;
  @Mock LoginUseCase loginUseCase;

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(new LocalValidatorFactoryBean())
            .build();
  }

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void 유효한_로그인요청을_받으면_로그인에_성공한다() throws Exception {
    // given
    final String email = "test@example.com";
    final String password = "Password123!";

    final LoginUseCase.Input input = new LoginUseCase.Input(email, password);
    final LoginUseCase.Output output = new LoginUseCase.Output("token");

    given(loginUseCase.execute(any())).willReturn(output);

    // when & then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("token"));

    then(loginUseCase).should().execute(any(LoginUseCase.Input.class));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void email이_비어있으면_400을_반환한다(String email) throws Exception {
    // given
    final LoginUseCase.Input input = new LoginUseCase.Input(email, "Password123!");

    // when & then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isBadRequest());

    then(loginUseCase).shouldHaveNoInteractions();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "   ", "\t", "\n"})
  void pw가_비어있으면_400을_반환한다(String pw) throws Exception {

    // given
    final LoginUseCase.Input input = new LoginUseCase.Input("email@test.com", pw);

    // when & then
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
        .andExpect(status().isBadRequest());

    then(loginUseCase).shouldHaveNoInteractions();
  }

  @Test
  void 인증정보가_올바르지_않으면_401을_반환한다() throws Exception {
    final String email = "test@example.com";
    final String password = "Password123!";

    given(loginUseCase.execute(any(LoginUseCase.Input.class)))
        .willThrow(new RestApiException(CustomErrorCode.INVALID_CREDENTIALS));

    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginUseCase.Input(email, password))))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.code").value(CustomErrorCode.INVALID_CREDENTIALS.name()));
  }
}
