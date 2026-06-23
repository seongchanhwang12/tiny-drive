package dev.chan.drive.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.chan.drive.app.auth.JwtPrincipal;
import dev.chan.drive.error.ApiException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;

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
    JwtPrincipal principal = new JwtPrincipal(userId);

    // when
    String token = sut.issue(principal);

    // then
    assertThat(token).isNotBlank();
  }

  @Test
  void 토큰에서_subject를_추출한다() {
    // given
    Long userId = 1L;
    JwtPrincipal principal = new JwtPrincipal(userId);
    String token = sut.issue(principal);

    // when
    JwtPrincipal result = sut.extractPrincipal(token);

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
    JwtPrincipal principal = new JwtPrincipal(userId);
    String token = sut.issue(principal);

    // when
    SecretKey otherKey = Jwts.SIG.HS256.key().build();
    JwtTokenProvider otherIssuer = new JwtTokenProvider(otherKey, properties);

    // then
    assertThatThrownBy(() -> otherIssuer.extractPrincipal(token))
        .isInstanceOf(AuthenticationException.class);
  }
}
