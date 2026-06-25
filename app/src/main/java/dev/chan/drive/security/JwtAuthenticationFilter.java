package dev.chan.drive.security;

import dev.chan.drive.app.auth.JwtPrincipal;
import dev.chan.drive.config.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtProvider;
  private final AuthenticationEntryPoint entryPoint;
  private static final String TOKEN_PREFIX = "Bearer ";

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain filterChain)
      throws ServletException, IOException {

    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String token = authorization.substring(TOKEN_PREFIX.length());

      JwtPrincipal principal = jwtProvider.extractPrincipal(token);

      Authentication authentication =
          new UsernamePasswordAuthenticationToken(principal, null, List.of());

      SecurityContextHolder.getContext().setAuthentication(authentication);

    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      entryPoint.commence(request, response, e);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
