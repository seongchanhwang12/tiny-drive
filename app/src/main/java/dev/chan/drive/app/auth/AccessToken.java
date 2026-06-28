package dev.chan.drive.app.auth;

public record AccessToken(String value, String type, long expiresIn) {}
