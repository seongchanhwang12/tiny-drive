package dev.chan.drive.app.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadUrlSigner implements StoragePort {

  @Override
  public PresignedUpload createPutUrl(String objectKey, String contentType) {
    // TODO : presign url 생성 처리 구현 예정
    return new PresignedUpload(null, null, null);
  }
}
