package dev.chan.drive.user;

import dev.chan.drive.common.errors.RestApiException;
import dev.chan.drive.common.errors.CustomErrorCode;
import org.springframework.stereotype.Component;

@Component
public class PasswordPolicy {
  public void validate(String pw) {

    if (pw == null
        || pw.isBlank()
        || pw.length() < 10
        || !containsUppercase(pw)
        || !containsSpecialCharacter(pw)) {

      throw new RestApiException(CustomErrorCode.INVALID_PASSWORD);
    }
  }

  private boolean containsSpecialCharacter(String pw) {
    return pw.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
  }

  private boolean containsUppercase(String pw) {
    return pw.chars().anyMatch(Character::isUpperCase);
  }
}
