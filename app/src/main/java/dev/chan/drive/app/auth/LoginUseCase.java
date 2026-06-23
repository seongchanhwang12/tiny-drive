package dev.chan.drive.app.auth;

import dev.chan.drive.app.user.User;
import dev.chan.drive.app.user.UserRepository;
import dev.chan.drive.config.JwtTokenProvider;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.ApiException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtTokenProvider tokenProvider;

  public record Input(@NotBlank @Email String email, @NotBlank String pw) {}

  public record Output(String accessToken) {}

  @Transactional(readOnly = true)
  public Output execute(final Input input) {
    final String rawPw = input.pw;

    final User user =
        userRepository
            .findByEmail(input.email)
            .filter(u -> passwordEncoder.matches(rawPw, u.getPw()))
            .orElseThrow(() -> new ApiException(CustomErrorCode.INVALID_CREDENTIALS));

    JwtPrincipal principal = JwtPrincipal.from(user);
    return new Output(tokenProvider.issue(principal));
  }
}
