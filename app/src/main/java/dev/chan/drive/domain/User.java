package dev.chan.drive.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
  private Long id;
  private String email;
  private String pw;

  private User(String email, String pw) {
    validateEmail(email);
    validatePassword(pw);

    this.email = email;
    this.pw = pw;
  }

  public static User register(String email, String pw) {
    return new User(email, pw);
  }

  private void validateEmail(String email) {
    if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")) {
      throw new InvalidEmailException("이메일 형식이 올바르지 않습니다");
    }
  }

  private void validatePassword(String pw) {

    if (pw == null || pw.isBlank()) {
      throw new InvalidPasswordException("비밀번호는 필수입니다.");
    }

    if (pw.length() < 10) {
      throw new InvalidPasswordException("비밀번호는 10자 이상이어야 합니다.");
    }

    if (!containsUppercase(pw)) {
      throw new InvalidPasswordException("대문자를 포함해야 합니다.");
    }

    if (!containsSpecialCharacter(pw)) {
      throw new InvalidPasswordException("특수문자를 포함해야 합니다.");
    }
  }

  private boolean containsSpecialCharacter(String pw) {
    return pw.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
  }

  private boolean containsUppercase(String pw) {
    return pw.chars().anyMatch(Character::isUpperCase);
  }
}
