package dev.chan.drive.config;

import dev.chan.drive.app.auth.Principal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

  @Bean
  AuditorAware<Long> auditorProvider() {
    return () -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !authentication.isAuthenticated()) {
        return Optional.empty();
      }

      Object principal = authentication.getPrincipal();

      if (!(principal instanceof Principal(Long userId))) {
        return Optional.empty();
      }
      return Optional.of(userId);
    };
  }
}
