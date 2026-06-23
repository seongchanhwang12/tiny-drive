package dev.chan.drive.config;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
  private final JwtProperties properties;

  @Bean
  SecretKey secretKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(properties.secret()));
  }
}
