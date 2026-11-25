package dev.chan.drive.security;

import dev.chan.drive.app.auth.Principal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockLoginUserFactory implements WithSecurityContextFactory<WithMockLoginUser> {
  @Override
  public SecurityContext createSecurityContext(final WithMockLoginUser annotation) {
    final Principal principal = new Principal(annotation.id());
    final var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of());

    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    return context;
  }
}
