package dev.chan.drive.app.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CommonErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DriveItemTest {

  private static final Long OWNER_ID = 1L;
  private static final Long FILE_SIZE = 100L;
  private static final ItemName FILE_NAME = ItemName.of("memo.txt");
  private static final String FILE_MIME_TYPE = "application/octet-stream";
  private static final ItemName FOLDER_NAME = ItemName.of("newfolder");
  private static final String FOLDER_MIME_TYPE = CustomMimeTypes.FOLDER;
  private static final Long DRIVE_ID = 10L;

  @Test
  void 파일이_부모로_주어지면_하위_항목_생성을_거부한다() {
    // Given
    DriveItem parent =
        DriveItem.builder()
            .driveId(DRIVE_ID)
            .mimeType("application/octet-stream")
            .name(ItemName.of("새문서"))
            .ownerId(OWNER_ID)
            .size(FILE_SIZE)
            .build();

    long folderSize = 0L;

    // When / Then
    assertThatThrownBy(
            () -> DriveItem.createUnder(parent, FILE_NAME, FOLDER_MIME_TYPE, folderSize, OWNER_ID))
        .isInstanceOf(ApiException.class);
  }

  @Test
  void 폴더_MIME_타입이_주어지면_폴더를_생성한다() {
    // Given
    DriveItem parent = DriveItemFixture.rootFolder(1L, 1L);
    long size = 0L;

    // When
    DriveItem newItem =
        DriveItem.createUnder(parent, FOLDER_NAME, FOLDER_MIME_TYPE, size, OWNER_ID);

    // Then
    assertThat(newItem.isFolder()).isTrue();
  }

  @Test
  void 크기가_0이_아닌_폴더가_주어지면_생성을_거부한다() {
    // Given
    DriveItem parent = DriveItemFixture.rootFolder(1L, 1L);
    long size = 1024L;

    // When / Then
    assertThatThrownBy(
            () -> DriveItem.createUnder(parent, FOLDER_NAME, FOLDER_MIME_TYPE, size, OWNER_ID))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception ->
                assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INVALID_STATE));
  }

  @ParameterizedTest
  @ValueSource(longs = {0, -1})
  void 파일_크기가_양수가_아니면_생성을_거부한다(final long fileSize) {
    // Given
    DriveItem parent = DriveItemFixture.rootFolder(1L, OWNER_ID);

    // When / Then
    assertThatThrownBy(
            () -> DriveItem.createUnder(parent, FILE_NAME, FILE_MIME_TYPE, fileSize, OWNER_ID))
        .isInstanceOf(ApiException.class);
  }

  @Test
  void 지원하지_않는_MIME_타입이_주어지면_항목_생성을_거부한다() {
    // Given
    DriveItem parent = DriveItemFixture.rootFolder(1L, 1L);
    String unsupportedType = "application/unsupported";

    // When / Then
    assertThatThrownBy(
            () -> DriveItem.createUnder(parent, FILE_NAME, unsupportedType, FILE_SIZE, OWNER_ID))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception ->
                assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.INVALID_PARAM));
  }
}
