package dev.chan.drive.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenResolver {

  private final JwtProperties jwtProperties;

  public Optional<String> extractAccessToken(HttpServletRequest request) {
    final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorization == null || !authorization.startsWith("Bearer ")) {
      return Optional.empty();
    }
    String token = authorization.substring("Bearer ".length()).trim();
    return Optional.of(token);
  }
}
