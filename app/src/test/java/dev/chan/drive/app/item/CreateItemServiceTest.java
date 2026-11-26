package dev.chan.drive.app.item;

import static dev.chan.drive.app.item.CreateItemService.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import dev.chan.drive.app.storage.PresignedUpload;
import dev.chan.drive.app.storage.StoragePort;
import dev.chan.drive.app.storage.UploadProperties;
import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CustomErrorCode;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.unit.DataSize;

@ExtendWith(MockitoExtension.class)
class CreateItemServiceTest {

  @InjectMocks CreateItemService sut;
  @Mock ItemRepository itemRepository;
  @Mock StoragePort storagePort;

  @Mock ParentFolderResolver parentFolderResolver;

  @Spy ItemNameResolver itemNameResolver = new ItemNameResolver();

  @Spy
  UploadProperties uploadProperties =
      new UploadProperties(DataSize.ofMegabytes(100), DataSize.ofMegabytes(100));

  public static final long USER_ID = 1L;
  public static final long PARENT_ID = 1L;
  public static final long FILE_SIZE = 10L;
  public static final String FILE_NAME = "memo.txt";
  public static final String MIME_TYPE = "application/octet-stream";

  @Test
  void 최대_파일_크기를_초과한_요청이_주어지면_항목_생성을_거부한다() {
    // Given
    final DataSize requestedFileSize = DataSize.ofMegabytes(101);
    final Input input = new Input(PARENT_ID, FILE_NAME, MIME_TYPE, requestedFileSize.toBytes());

    // When / Then
    assertThatThrownBy(() -> sut.create(USER_ID, input))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception ->
                assertThat(exception.getErrorCode())
                    .isEqualTo(CustomErrorCode.FILE_SIZE_LIMIT_EXCEEDED));
    then(itemRepository).should(never()).save(any(DriveItem.class));
    then(storagePort).should(never()).createPutUrl(anyString(), eq(MIME_TYPE));
  }

  @Test
  void 유효한_파일_정보가_주어지면_항목을_저장하고_업로드_URL을_반환한다() {
    // Given
    final DriveItem parent = DriveItemFixture.folderWithId(PARENT_ID, 0L, USER_ID);
    final Input input = new Input(PARENT_ID, FILE_NAME, MIME_TYPE, FILE_SIZE);
    final DriveItem file = DriveItemFixture.file(PARENT_ID, FILE_NAME, USER_ID);
    final PresignedUpload upload = PresignUploadFixture.putUrl();

    given(parentFolderResolver.resolve(PARENT_ID, USER_ID)).willReturn(parent);
    given(itemRepository.save(any(DriveItem.class))).willReturn(file);
    given(storagePort.createPutUrl(anyString(), eq(MIME_TYPE))).willReturn(upload);

    // When
    final Output output = sut.create(USER_ID, input);

    // Then
    final ArgumentCaptor<DriveItem> savedItem = ArgumentCaptor.forClass(DriveItem.class);
    final ArgumentCaptor<String> storageKey = ArgumentCaptor.forClass(String.class);
    then(itemRepository).should().save(savedItem.capture());
    then(storagePort).should().createPutUrl(storageKey.capture(), eq(MIME_TYPE));

    assertThat(savedItem.getValue())
        .extracting(
            DriveItem::getMimeType,
            DriveItem::getParentId,
            DriveItem::getOwnerId,
            DriveItem::getName,
            DriveItem::getSize)
        .containsExactly(MIME_TYPE, PARENT_ID, USER_ID, ItemName.of(FILE_NAME), FILE_SIZE);

    assertThat(output.upload()).isEqualTo(upload);
  }

  @Test
  void 폴더_생성_요청이_주어지면_업로드_URL을_생성하지_않는다() {
    // Given
    final Input input = new Input(PARENT_ID, FILE_NAME, CustomMimeTypes.FOLDER, 0);
    final DriveItem parent = DriveItemFixture.folderWithId(PARENT_ID, null, USER_ID);

    given(parentFolderResolver.resolve(PARENT_ID, USER_ID)).willReturn(parent);

    // When
    final Output output = sut.create(USER_ID, input);

    // Then
    assertThat(output.upload()).isNull();
    then(storagePort).should(never()).createPutUrl(anyString(), eq(CustomMimeTypes.FOLDER));
  }
}
