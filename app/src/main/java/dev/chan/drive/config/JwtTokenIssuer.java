package dev.chan.drive.config;

import dev.chan.drive.app.user.User;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenIssuer {

  private final SecretKey secretKey;

  public String issue(User user) {
    return Jwts.builder().subject(user.getEmail()).signWith(secretKey).compact();
  }
}
