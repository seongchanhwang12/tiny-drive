package dev.chan.drive.app.item;

import java.time.Instant;
import java.util.Map;

public record CreateItemResponse(long itemId, UploadData upload) {

  public static CreateItemResponse from(final CreateItemService.Output output) {
    return new CreateItemResponse(
        output.item().getId(),
        new UploadData(
            output.upload().url().toString(),
            output.upload().requiredHeaders(),
            output.upload().expiresAt()));
  }

  public record UploadData(String url, Map<String, String> requiredHeaders, Instant expiresAt) {}
}
