package dev.chan.drive.app.auth;

import dev.chan.drive.app.user.User;

public record JwtPrincipal(Long userId) {
  public static JwtPrincipal from(User user) {
    return new JwtPrincipal(user.getId());
  }

  public String getSubject() {
    return userId.toString();
  }
}
