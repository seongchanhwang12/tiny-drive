package dev.chan.drive.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("사용자")
class UserTest {

  private static final String VALID_EMAIL = "user@example.com";
  private static final String VALID_PASSWORD = "Password123!";

  @DisplayName("유효한 이메일과 비밀번호로 가입하면 사용자가 생성된다")
  @Test
  void register_user() {
    User user = User.register(VALID_EMAIL, VALID_PASSWORD);

    assertThat(user.getId()).isNull();
    assertThat(user.getEmail()).isEqualTo(VALID_EMAIL);
    assertThat(user.getPw()).isEqualTo(VALID_PASSWORD);
  }
}
