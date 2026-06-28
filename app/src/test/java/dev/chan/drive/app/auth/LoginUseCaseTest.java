package dev.chan.drive.app.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import dev.chan.drive.app.user.User;
import dev.chan.drive.app.user.UserRepository;
import dev.chan.drive.config.JwtTokenProvider;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.ApiException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

  private static final String EMAIL = "email@email.com";
  private static final String RAW_PASSWORD = "email@email.com";
  private static final String ENCODED_PASSWORD = "encoded-password";

  @InjectMocks LoginUseCase sut;
  @Mock PasswordEncoder passwordEncoder;
  @Mock UserRepository userRepository;
  @Mock JwtTokenProvider tokenProvider;

  @Test
  void 비밀번호가_일치하지_않으면_로그인에_실패한다() {
    // given
    final User user = User.register(EMAIL, ENCODED_PASSWORD);

    given(userRepository.findByEmail(EMAIL)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).willReturn(false);

    // when
    final ApiException result =
        assertThrows(
            ApiException.class, () -> sut.execute(new LoginUseCase.Input(EMAIL, RAW_PASSWORD)));

    // then
    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_CREDENTIALS);
  }

  @Test
  void 사용자가_없으면_로그인에_실패한다() {
    // given
    given(userRepository.findByEmail(EMAIL)).willReturn(Optional.empty());

    // when
    final ApiException result =
        assertThrows(
            ApiException.class, () -> sut.execute(new LoginUseCase.Input(EMAIL, RAW_PASSWORD)));

    // then
    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_CREDENTIALS);
  }

  @Test
  void 로그인_성공() {
    // given
    final User user = User.register(EMAIL, ENCODED_PASSWORD);
    ReflectionTestUtils.setField(user, "id", 1L);

    final Principal principal = Principal.from(user);
    final AccessToken accessToken = new AccessToken("token", "bearer", 1800L);

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    given(passwordEncoder.matches(RAW_PASSWORD, ENCODED_PASSWORD)).willReturn(true);
    given(tokenProvider.issue(principal)).willReturn(accessToken);

    // when
    AccessToken result = sut.execute(new LoginUseCase.Input(EMAIL, RAW_PASSWORD));

    // then
    assertThat(result.value()).isEqualTo("token");
    then(userRepository).should().findByEmail(EMAIL);

    then(passwordEncoder).should().matches(RAW_PASSWORD, ENCODED_PASSWORD);
    then(tokenProvider).should().issue(principal);
  }
}
