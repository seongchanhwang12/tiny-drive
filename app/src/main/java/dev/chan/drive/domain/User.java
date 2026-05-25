package dev.chan.drive.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
    name = "users",
    uniqueConstraints = {@UniqueConstraint(name = "uk_user_email", columnNames = "email")})
public class User extends Common {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String pw;

  private User(String email, String pw) {
    this.email = email;
    this.pw = pw;
  }

  public static User register(String email, String pw) {
    return new User(email, pw);
  }
}
