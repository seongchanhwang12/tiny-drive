package dev.chan.drive.app.item;

import dev.chan.drive.app.drive.Drive;
import dev.chan.drive.app.drive.DriveRepository;
import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CommonErrorCode;
import dev.chan.drive.error.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParentFolderResolver {
  private final ItemRepository itemRepository;
  private final DriveRepository driveRepository;

  /**
   * Parent 를 찾아 반환한다 - parent ID가 null이면 사용자 드라이브의 루트 경로에 파일을 업로드 한다.
   *
   * @param parentId - 부모 폴더 ID
   * @param userId - 사용자 ID
   * @return
   */
  public DriveItem resolve(Long parentId, Long userId) {
    if (parentId == null) {
      Drive drive =
          driveRepository
              .findByOwnerId(userId)
              .orElseThrow(
                  () -> new ApiException(CustomErrorCode.DRIVE_NOT_FOUND, "userId = " + userId));

      return itemRepository
          .findByDriveIdAndParentIdIsNull(drive.getId())
          .orElseThrow(
              () -> new ApiException(CustomErrorCode.ROOT_NOT_FOUND, "driveId = " + drive.getId()));
    }
    DriveItem parent = itemRepository
        .findById(parentId)
        .orElseThrow(() -> new ApiException(CommonErrorCode.NOT_FOUND, "parentId = " + parentId));

    if (parent.getOwnerId() != userId) {
      throw new ApiException(CommonErrorCode.NOT_FOUND, "parentId = " + parentId);
    }

    if (!parent.isFolder()) {
      throw new ApiException(CustomErrorCode.PARENT_NOT_FOLDER);
    }

    return parent;
  }
}
