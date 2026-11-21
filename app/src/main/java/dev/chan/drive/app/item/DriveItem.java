package dev.chan.drive.app.item;

import dev.chan.drive.app.common.BaseEntity;
import dev.chan.drive.app.common.Guard;
import dev.chan.drive.app.drive.Drive;
import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CommonErrorCode;
import dev.chan.drive.error.CustomErrorCode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import java.util.Set;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DriveItem extends BaseEntity {

  private static final Set<String> ALLOWED_MIME_TYPES =
      Set.of(
          "application/octet-stream",
          "text/plain",
          "image/jpeg",
          "image/png",
          "application/pdf",
          CustomMimeTypes.FOLDER);

  private Long driveId;
  private Long parentId;
  @Embedded private ItemName name;
  private String mimeType;

  private long size;
  private long ownerId;

  @Builder
  private DriveItem(
      Long driveId, Long parentId, ItemName name, String mimeType, long size, Long ownerId) {

    this.driveId = Guard.notNull(driveId);
    this.parentId = parentId;
    this.name = Guard.notNull(name);
    this.mimeType = Guard.notNull(mimeType);
    this.size = size;
    this.ownerId = Guard.notNull(ownerId);
    validate(size);
  }

  public static DriveItem createRoot(long driveId, long userId) {
    return DriveItem.builder()
        .driveId(driveId)
        .ownerId(userId)
        .parentId(null)
        .name(ItemName.of("My Drive"))
        .mimeType(CustomMimeTypes.FOLDER)
        .size(0L)
        .build();
  }

  /**
   * 부모 폴더 아래에 새 항목을 생성한다.
   *
   * @param parent 부모 폴더
   * @param newItemName 새 항목의 이름
   * @param mimeType 파일의 MIME 타입
   * @param size 파일 크기(바이트)
   * @param ownerId 소유자 ID
   * @return 생성된 항목
   * @throws ApiException 부모 항목이 폴더가 아닌 경우
   */
  public static DriveItem createUnder(
      DriveItem parent, ItemName newItemName, String mimeType, long size, Long ownerId) {
    String normalizedMimeType = normalize(mimeType);

    if (!parent.isFolder()) {
      throw new ApiException(CommonErrorCode.INVALID_STATE, "Parent item must be a folder.");
    }

    return new DriveItem(
        parent.getDriveId(), parent.getId(), newItemName, normalizedMimeType, size, ownerId);
  }

  /** mimeType 을 정규화해 정규화한 값을 반환한다 */
  private static String normalize(final String raw) {
    try {
      MimeType parsed = MimeTypeUtils.parseMimeType(raw);
      return parsed.getType() + "/" + parsed.getSubtype();
    } catch (InvalidMimeTypeException e) {
      throw new ApiException(CommonErrorCode.INVALID_PARAM, "invalid mimeType = " + raw);
    }
  }

  /**
   * 항목의 MIME 타입과 크기를 검증한다.
   *
   * <p>폴더의 크기는 0이어야 하며, 파일의 크기는 양수여야 한다. MIME 타입은 {@code ALLOWED_MIME_TYPES}에 포함되어야 한다.
   *
   * @param size 항목의 크기(바이트)
   * @throws ApiException MIME 타입 또는 크기가 유효하지 않은 경우
   */
  private void validate(long size) {
    if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
      throw new ApiException(
          CommonErrorCode.INVALID_PARAM, "unsupported mimeType. mimeType = " + mimeType);
    }

    if (isFolder() && size != 0) {
      throw new ApiException(CommonErrorCode.INVALID_STATE, "Folder cannot have a size");
    }

    if (!isFolder() && size <= 0) {
      throw new ApiException(CommonErrorCode.INVALID_PARAM, "Size must be positive");
    }
  }

  public boolean isFolder() {
    return mimeType.equals(CustomMimeTypes.FOLDER);
  }

  public boolean isRoot() {
    return isFolder() && parentId == null;
  }
}
