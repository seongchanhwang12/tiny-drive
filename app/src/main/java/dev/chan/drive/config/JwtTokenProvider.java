package dev.chan.drive.config;

import dev.chan.drive.app.auth.AccessToken;
import dev.chan.drive.app.auth.Principal;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.sql.Date;
import java.time.Instant;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private final SecretKey secretKey;
  private final JwtProperties jwtProperties;

  public AccessToken issue(final Principal principal) {
    final Instant now = Instant.now();
    final long expireSeconds = jwtProperties.accessTokenTtl().getSeconds();

    final String value =
        Jwts.builder()
            .issuer(jwtProperties.issuer())
            .subject(principal.getSubject())
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expireSeconds)))
            .signWith(secretKey)
            .compact();

    return new AccessToken(value, "Bearer", expireSeconds);
  }

  public Principal extractPrincipal(String token) {
    try {
      String subject =
          Jwts.parser()
              .verifyWith(secretKey)
              .requireIssuer(jwtProperties.issuer())
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();

      return new Principal(Long.valueOf(subject));
    } catch (JwtException | IllegalArgumentException e) {
      throw new BadCredentialsException("Invalid token", e);
    }
  }
}
