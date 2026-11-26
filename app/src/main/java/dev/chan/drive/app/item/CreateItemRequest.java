package dev.chan.drive.app.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateItemRequest(
    Long parentId, @NotBlank String mimeType, @NotBlank String fileName, @Positive long fileSize) {}
