package dev.chan.drive.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import dev.chan.drive.errors.CustomErrorCode;
import dev.chan.drive.errors.RestApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordPolicyTest {

  PasswordPolicy policy = new PasswordPolicy();

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"invalid"})
  void validate_fail(String rawPassword) {
    RestApiException result =
        assertThrows(RestApiException.class, () -> policy.validate(rawPassword));

    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.INVALID_PASSWORD);
  }

  @Test
  void should_pass_when_password_satisfies_policy() {
    assertThatCode(() -> policy.validate("Password123!")).doesNotThrowAnyException();
  }
}
