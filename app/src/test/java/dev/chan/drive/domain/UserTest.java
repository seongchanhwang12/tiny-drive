package dev.chan.drive.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

  @DisplayName("유효하지 않은 이메일이면 가입할 수 없다")
  @ParameterizedTest
  @ValueSource(strings = {"", " ", "invalid-email", "user.example.com", "user@example"})
  void fail_when_email_is_invalid(String invalidEmail) {
    assertThatThrownBy(() -> User.register(invalidEmail, VALID_PASSWORD))
        .isInstanceOf(InvalidEmailException.class);
  }

  @DisplayName("비밀번호는 필수값이다")
  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  void fail_when_password_is_blank(String invalidPassword) {
    assertThatThrownBy(() -> User.register(VALID_EMAIL, invalidPassword))
        .isInstanceOf(InvalidPasswordException.class);
  }

  @DisplayName("비밀번호는 10자 이상이어야 한다")
  @Test
  void fail_when_password_is_shorter_than_10_characters() {
    assertThatThrownBy(() -> User.register(VALID_EMAIL, "Pass123!"))
        .isInstanceOf(InvalidPasswordException.class);
  }

  @DisplayName("비밀번호는 대문자를 포함해야 한다")
  @Test
  void fail_when_password_does_not_contain_uppercase() {
    assertThatThrownBy(() -> User.register(VALID_EMAIL, "password123!"))
        .isInstanceOf(InvalidPasswordException.class);
  }

  @DisplayName("비밀번호는 특수문자를 포함해야 한다")
  @Test
  void fail_when_password_does_not_contain_special_character() {
    assertThatThrownBy(() -> User.register(VALID_EMAIL, "Password123"))
        .isInstanceOf(InvalidPasswordException.class);
  }
}
