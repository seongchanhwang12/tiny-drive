package dev.chan.drive.app.auth;

import dev.chan.drive.app.user.User;

public record Principal(Long userId) {
  public static Principal from(User user) {
    return new Principal(user.getId());
  }

  public String getSubject() {
    return userId.toString();
  }
}
