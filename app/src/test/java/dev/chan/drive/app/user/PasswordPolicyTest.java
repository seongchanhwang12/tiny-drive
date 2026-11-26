package dev.chan.drive.app.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.error.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordPolicyTest {

  PasswordPolicy policy = new PasswordPolicy();

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid"})
  void 비밀번호_정책을_충족하지_못하면_비밀번호를_거부한다(String rawPassword) {
    // When / Then
    assertThatThrownBy(() -> policy.validate(rawPassword))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception ->
                assertThat(exception.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_PASSWORD));
  }

  @Test
  void 비밀번호_정책을_충족하면_검증을_통과한다() {
    // When / Then
    assertThatCode(() -> policy.validate("Password123!")).doesNotThrowAnyException();
  }
}
