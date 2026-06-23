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

  @InjectMocks LoginUseCase sut;
  @Mock PasswordEncoder passwordEncoder;
  @Mock UserRepository userRepository;
  @Mock JwtTokenProvider tokenProvider;

  @Test
  void 비밀번호가_일치하지_않으면_로그인에_실패한다() {
    // given
    String email = "test@test.com";
    String rawPw = "Password1!";
    String encodedPw = "encoded-password";

    User user = User.register(email, encodedPw);

    given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
    given(passwordEncoder.matches(rawPw, encodedPw)).willReturn(false);

    // when
    ApiException result =
        assertThrows(ApiException.class, () -> sut.execute(new LoginUseCase.Input(email, rawPw)));

    // then
    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_CREDENTIALS);
  }

  @Test
  void 사용자가_없으면_로그인에_실패한다() {
    // given
    String email = "test@test.com";
    String rawPw = "Password1!";

    given(userRepository.findByEmail(email)).willReturn(Optional.empty());

    // when
    ApiException result =
        assertThrows(ApiException.class, () -> sut.execute(new LoginUseCase.Input(email, rawPw)));

    // then
    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_CREDENTIALS);
  }

  @Test
  void 로그인_성공() {
    // given
    String email = "test@test.com";
    String rawPw = "Password1!";
    String encodedPw = "encoded-password";

    User user = User.register(email, encodedPw);
    ReflectionTestUtils.setField(user, "id", 1L);

    JwtPrincipal principal = JwtPrincipal.from(user);

    given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    given(passwordEncoder.matches(rawPw, encodedPw)).willReturn(true);
    given(tokenProvider.issue(principal)).willReturn("token");

    // when
    LoginUseCase.Output result = sut.execute(new LoginUseCase.Input(email, rawPw));

    // then
    assertThat(result.accessToken()).isEqualTo("token");
    then(userRepository).should().findByEmail(email);

    then(passwordEncoder).should().matches(rawPw, encodedPw);
    then(tokenProvider).should().issue(principal);
  }
}
