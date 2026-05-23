package dev.chan.drive.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chan.drive.app.RegisterUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class RegisterUserControllerTest {

  private MockMvc mockMvc;

  @Mock private RegisterUserUseCase registerUserUseCase;

  @InjectMocks private UserController registerUserController;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(registerUserController).build();
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
}
