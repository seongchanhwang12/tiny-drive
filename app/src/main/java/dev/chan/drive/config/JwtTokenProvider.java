package dev.chan.drive.config;

import dev.chan.drive.app.auth.JwtPrincipal;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.ApiException;
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

  public String issue(JwtPrincipal principal) {
    Instant now = Instant.now();

    return Jwts.builder()
        .issuer(jwtProperties.issuer())
        .subject(principal.getSubject())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(jwtProperties.accessTokenTtl())))
        .signWith(secretKey)
        .compact();
  }

  public JwtPrincipal extractPrincipal(String token) {
    try {
      String subject =
          Jwts.parser()
              .verifyWith(secretKey)
              .requireIssuer("tiny-drive")
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getSubject();

      return new JwtPrincipal(Long.valueOf(subject));
    } catch (JwtException | IllegalArgumentException e) {
      throw new BadCredentialsException("Invalid token", e);
    }
  }
}
