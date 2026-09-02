package dev.chan.drive.config;

import dev.chan.drive.app.auth.AccessToken;
import dev.chan.drive.app.auth.Principal;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

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
  void 토큰을_정상적으로_발급한다() {
    // given
    Long userId = 1L;
    Principal principal = new Principal(userId);

    // when
    AccessToken token = sut.issue(principal);
    Principal extracted = sut.extractPrincipal(token.value());

    // then
    assertThat(token.value()).isNotBlank();
    assertThat(token.type()).isEqualTo("Bearer");
    assertThat(token.expiresIn()).isEqualTo(1800L);
    assertThat(extracted.userId()).isEqualTo(userId);
  }

  @Test
  void 토큰에서_subject를_추출한다() {
    // given
    Long userId = 1L;
    Principal principal = new Principal(userId);
    AccessToken issue = sut.issue(principal);

    // when
    Principal result = sut.extractPrincipal(issue.value());

    // then
    assertThat(result.getSubject()).isEqualTo("1");
  }

  @Test
  void 잘못된_토큰이면_subject_추출에_실패한다() {
    assertThatThrownBy(() -> sut.extractPrincipal("invalid-token"))
        .isInstanceOf(AuthenticationException.class);
  }

  @Test
  void 다른_secretKey로_발급된_토큰은_검증에_실패한다() {
    // given
    Long userId = 1L;
    Principal principal = new Principal(userId);
    AccessToken issue = sut.issue(principal);

    // when
    SecretKey otherKey = Jwts.SIG.HS256.key().build();
    JwtTokenProvider otherIssuer = new JwtTokenProvider(otherKey, properties);

    // then
    assertThatThrownBy(() -> otherIssuer.extractPrincipal(issue.value()))
        .isInstanceOf(AuthenticationException.class);
  }
}
