package dev.chan.drive.app.item;

import dev.chan.drive.app.storage.PresignedUpload;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

public class PresignUploadFixture {
  public static PresignedUpload putUrl() {
    return new PresignedUpload(
        URI.create("https://example.com/upload"),
        Map.of("Content-Type", "application/octet-stream"),
        Instant.now().plusSeconds(3600));
  }
}
