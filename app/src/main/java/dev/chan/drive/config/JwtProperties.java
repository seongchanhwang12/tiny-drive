package dev.chan.drive.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String issuer, String secret, Duration accessTokenTtl) {}
