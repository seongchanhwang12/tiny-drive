package dev.chan.drive.security;

import dev.chan.drive.app.auth.Principal;
import dev.chan.drive.config.JwtProperties;
import dev.chan.drive.config.JwtTokenProvider;
import dev.chan.drive.config.JwtTokenResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final JwtTokenResolver jwtTokenResolver;
  private final AuthenticationEntryPoint entryPoint;

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Optional<String> tokenOpt = jwtTokenResolver.extractAccessToken(request);
      // 토큰이 비어있을 경우 permitAll 요청일 수 있므로 Authorization 필터에서 판단하도록 체인을 넘깁니다.
      if (tokenOpt.isEmpty()) {
        filterChain.doFilter(request, response);
        return;
      }

      final Authentication authentication = attemptAuthentication(tokenOpt.get());

      // securityContextHolderFilter 가 등록되어 있으므로, 새로 생성하지않고 홀더에서 바로 꺼내서 사용합니다.
      final SecurityContext context = SecurityContextHolder.getContext();
      context.setAuthentication(authentication);
    } catch (AuthenticationException e) {
      SecurityContextHolder.clearContext();
      entryPoint.commence(request, response, e);
      return;
    }

    filterChain.doFilter(request, response);
  }

  public Authentication attemptAuthentication(String token) {
    //  편리하게 Authentication 을 사용하기 위해 따로 구현하지않고 UsernamePasswordAuthenticationToken 을 사용합니다.
    final Principal principal = jwtTokenProvider.extractPrincipal(token);
    return new UsernamePasswordAuthenticationToken(principal, null, List.of());
  }
}
