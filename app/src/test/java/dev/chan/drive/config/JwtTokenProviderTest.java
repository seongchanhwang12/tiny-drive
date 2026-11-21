package dev.chan.drive.config;

import dev.chan.drive.app.auth.AccessToken;
import dev.chan.drive.app.auth.Principal;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

  private JwtTokenProvider sut;
  private JwtProperties properties;

  @BeforeEach
  void setUp() {
    SecretKey key = Jwts.SIG.HS256.key().build();
    properties = new JwtProperties("tiny-drive", "test-secret-key", Duration.ofMinutes(30));

    sut = new JwtTokenProvider(key, properties);
  }

  @Test
  void 유효한_주체가_주어지면_토큰을_발급하고_주체를_복원한다() {
    // Given
    Long userId = 1L;
    Principal principal = new Principal(userId);

    // When
    AccessToken token = sut.issue(principal);
    Principal extracted = sut.extractPrincipal(token.value());

    // Then
    assertThat(token.value()).isNotBlank();
    assertThat(token.type()).isEqualTo("Bearer");
    assertThat(token.expiresIn()).isEqualTo(1800L);
    assertThat(extracted.userId()).isEqualTo(userId);
  }

  @Test
  void 발급된_토큰이_주어지면_subject를_추출한다() {
    // Given
    Long userId = 1L;
    Principal principal = new Principal(userId);
    AccessToken issue = sut.issue(principal);

    // When
    Principal result = sut.extractPrincipal(issue.value());

    // Then
    assertThat(result.getSubject()).isEqualTo("1");
  }

  @Test
  void 유효하지_않은_토큰이_주어지면_주체_추출을_거부한다() {
    // When / Then
    assertThatThrownBy(() -> sut.extractPrincipal("invalid-token"))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void 다른_비밀키로_발급된_토큰이_주어지면_검증을_거부한다() {
    // Given
    Long userId = 1L;
    Principal principal = new Principal(userId);
    AccessToken issue = sut.issue(principal);

    // When
    SecretKey otherKey = Jwts.SIG.HS256.key().build();
    JwtTokenProvider otherIssuer = new JwtTokenProvider(otherKey, properties);

    // Then
    assertThatThrownBy(() -> otherIssuer.extractPrincipal(issue.value()))
        .isInstanceOf(AuthenticationException.class);
  }
}
