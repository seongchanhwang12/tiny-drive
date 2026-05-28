package dev.chan.drive.app.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.drive.error.RestApiException;
import dev.chan.drive.error.GlobalExceptionHandler;
import dev.chan.drive.error.CommonErrorCode;
import dev.chan.drive.error.CustomErrorCode;
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
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock private RegisterUserUseCase registerUserUseCase;

  @InjectMocks private UserController registerUserController;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(registerUserController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(new LocalValidatorFactoryBean())
            .build();
  }

  @Test
  void register_user() throws Exception {
    // given
    RegisterUserUseCase.Input request =
        new RegisterUserUseCase.Input("test@example.com", "Password123!");

    RegisterUserUseCase.Output response =
        new RegisterUserUseCase.Output(1L, 10L, "test@example.com");

    given(registerUserUseCase.execute(any())).willReturn(response);

    // when & then
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(10L))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.driveId").value(1L));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalidMail.com", "invalid", "user@", "@gmail.com"})
  void should_fail_when_email_is_invalid(String invalidEmail) throws Exception {
    // given
    RegisterUserUseCase.Input request = new RegisterUserUseCase.Input(invalidEmail, "Password123!");

    // when & then
    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_PARAM.name()))
        .andExpect(jsonPath("$.message").isNotEmpty())
        .andExpect(jsonPath("$.errors[0].field").value("email"))
        .andExpect(jsonPath("$.errors[0].message").isNotEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"password1", "!", "P!1"})
  void should_fail_when_password_is_invalid(String invalidPassword) throws Exception {
    // given
    RegisterUserUseCase.Input request =
        new RegisterUserUseCase.Input("valid@email.com", invalidPassword);

    willThrow(new RestApiException(CustomErrorCode.INVALID_PASSWORD))
        .given(registerUserUseCase)
        .execute(any(RegisterUserUseCase.Input.class));

    mockMvc
        .perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(CustomErrorCode.INVALID_PASSWORD.name()))
        .andExpect(jsonPath("$.message").isNotEmpty());
  }
}
