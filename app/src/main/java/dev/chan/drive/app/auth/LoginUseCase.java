package dev.chan.drive.app.auth;

import dev.chan.drive.app.user.User;
import dev.chan.drive.app.user.UserRepository;
import dev.chan.drive.config.JwtTokenIssuer;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.RestApiException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtTokenIssuer jwtTokenIssuer;

  public record Input(@NotBlank String email, @NotBlank String pw) {}

  public record Output(String accessToken) {}

  public Output execute(final Input input) {
    final String rawPw = input.pw;

    final User user =
        userRepository
            .findByEmail(input.email)
            .filter(u -> passwordEncoder.matches(rawPw, u.getPw()))
            .orElseThrow(() -> new RestApiException(CustomErrorCode.INVALID_CREDENTIALS));

    return new Output(jwtTokenIssuer.issue(user));
  }
}
