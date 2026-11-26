package dev.chan.drive.app.item;

import dev.chan.drive.app.drive.Drive;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class DriveItemFixture {

  private static final long ANY_DRIVE_ID = 1L;
  public static final String ANY_FOLDER_NAME = "새폴더";

  public static DriveItem rootFolder(long driveId, long userId) {
    return DriveItem.createRoot(driveId, userId);
  }

  public static DriveItem file(long parentId, String name, long userId) {
    DriveItem file =
        DriveItem.builder()
            .driveId(ANY_DRIVE_ID)
            .name(ItemName.of(name))
            .size(10L)
            .mimeType("text/plain")
            .parentId(parentId)
            .ownerId(userId)
            .build();

    ReflectionTestUtils.setField(file, "id", 1L);

    return file;
  }

  public static DriveItem folderWithId(Long id, Long parentId, long userId) {
    DriveItem item = folder(parentId, ANY_FOLDER_NAME, userId);
    ReflectionTestUtils.setField(item, "id", id);
    return item;
  }

  public static DriveItem folder(Long parentId, String name, long userId) {
    return DriveItem.builder()
        .driveId(ANY_DRIVE_ID)
        .name(ItemName.of(name))
        .size(0L)
        .mimeType(CustomMimeTypes.FOLDER)
        .parentId(parentId)
        .ownerId(userId)
        .build();
  }
}
