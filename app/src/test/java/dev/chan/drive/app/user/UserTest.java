package dev.chan.drive.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserTest {

  private static final String VALID_EMAIL = "user@example.com";
  private static final String VALID_PASSWORD = "Password123!";

  @Test
  void 유효한_이메일과_비밀번호가_주어지면_사용자가_생성된다() {
    // When
    User user = User.register(VALID_EMAIL, VALID_PASSWORD);

    // Then
    // Then
    assertThat(user.getId()).isNull();
    assertThat(user.getEmail()).isEqualTo(VALID_EMAIL);
    assertThat(user.getPw()).isEqualTo(VALID_PASSWORD);
  }
}
