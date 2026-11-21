package dev.chan.drive.app.storage;

public interface StoragePort {

  PresignedUpload createPutUrl(String objectKey, String contentType);
}
