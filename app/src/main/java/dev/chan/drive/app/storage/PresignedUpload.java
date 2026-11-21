package dev.chan.drive.app.storage;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

public record PresignedUpload(URI url, Map<String, String> requiredHeaders, Instant expiresAt) {}
