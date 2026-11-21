package dev.chan.drive.app.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

@ConfigurationProperties(prefix = "storage.upload")
public record UploadProperties(DataSize maxFileSize, DataSize singlePutThreshold) {}
