package dev.chan.drive.app.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import dev.chan.drive.app.drive.Drive;
import dev.chan.drive.app.drive.DriveFixture;
import dev.chan.drive.app.drive.DriveRepository;
import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CommonErrorCode;
import dev.chan.drive.error.CustomErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParentFolderResolverTest {

  private static final long USER_ID = 1L;
  private static final String PARENT_FOLDER_NAME = "parent";

  @InjectMocks ParentFolderResolver sut;
  @Mock private ItemRepository itemRepository;
  @Mock private DriveRepository driveRepository;

  @Nested
  @DisplayName("요청에 지정된 부모가 없을 때")
  class WhenParentIdIsNull {

    @Test
    void 부모_ID가_없으면_사용자_문서함의_루트_폴더를_반환한다() {
      // Given
      final Drive drive = DriveFixture.personalDrive(USER_ID);
      final DriveItem root = DriveItemFixture.rootFolder(drive.getId(), USER_ID);

      given(driveRepository.findByOwnerId(USER_ID)).willReturn(Optional.of(drive));
      given(itemRepository.findByDriveIdAndParentIdIsNull(drive.getId()))
          .willReturn(Optional.of(root));

      // When
      final DriveItem result = sut.resolve(null, USER_ID);

      // Then
      assertThat(result.getDriveId()).isEqualTo(drive.getId());
      assertThat(result.getOwnerId()).isEqualTo(drive.getOwnerId());
      assertThat(result.isRoot()).isTrue();
    }

    @Test
    void 사용자_문서함이_없으면_루트_폴더_조회에_실패한다() {
      // When / Then
      assertThatThrownBy(() -> sut.resolve(null, USER_ID))
          .isInstanceOfSatisfying(
              ApiException.class,
              exception -> assertThat(exception.getErrorCode()).isEqualTo(CustomErrorCode.DRIVE_NOT_FOUND));
    }

    @Test
    void 문서함에_루트_폴더가_없으면_루트_폴더_조회에_실패한다() {
      // Given
      final Drive personalDrive = DriveFixture.personalDrive(USER_ID);
      given(driveRepository.findByOwnerId(USER_ID)).willReturn(Optional.of(personalDrive));

      // When / Then
      assertThatThrownBy(() -> sut.resolve(null, USER_ID))
          .isInstanceOfSatisfying(
              ApiException.class,
              exception -> assertThat(exception.getErrorCode()).isEqualTo(CustomErrorCode.ROOT_NOT_FOUND));
    }
  }

  @Test
  void 유효한_부모_ID가_주어지면_해당_항목을_반환한다() {
    // Given
    final long parentId = 1L;
    final DriveItem parent = DriveItemFixture.folder(parentId, PARENT_FOLDER_NAME, USER_ID);
    given(itemRepository.findById(parentId)).willReturn(Optional.of(parent));

    // When
    final DriveItem result = sut.resolve(parentId, USER_ID);

    // Then
    assertThat(result).isSameAs(parent);
    then(itemRepository).should().findById(parentId);
  }

  @Test
  void 다른_사용자_소유의_부모_ID가_주어지면_조회에_실패한다() {
    // Given
    final long parentId = 1L;
    final DriveItem parent = DriveItemFixture.folder(parentId, PARENT_FOLDER_NAME, USER_ID + 1);
    given(itemRepository.findById(parentId)).willReturn(Optional.of(parent));

    // When / Then
    assertThatThrownBy(() -> sut.resolve(parentId, USER_ID))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception -> assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.NOT_FOUND));
  }

  @Test
  void 부모_ID가_파일을_가리키면_폴더로_사용할_수_없다() {
    // Given
    final long parentId = 1L;
    final DriveItem parent =
        DriveItemFixture.file(parentId, PARENT_FOLDER_NAME, USER_ID);
    given(itemRepository.findById(parentId)).willReturn(Optional.of(parent));

    // When / Then
    assertThatThrownBy(() -> sut.resolve(parentId, USER_ID))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception ->
                assertThat(exception.getErrorCode()).isEqualTo(CustomErrorCode.PARENT_NOT_FOLDER));
  }

  @Test
  void 존재하지_않는_부모_ID가_주어지면_조회에_실패한다() {
    // Given
    given(itemRepository.findById(1L)).willReturn(Optional.empty());

    // When / Then
    assertThatThrownBy(() -> sut.resolve(1L, USER_ID))
        .isInstanceOfSatisfying(
            ApiException.class,
            exception -> assertThat(exception.getErrorCode()).isEqualTo(CommonErrorCode.NOT_FOUND));
    then(driveRepository).should(never()).findByOwnerId(anyLong());
    then(itemRepository).should(never()).findByDriveIdAndParentIdIsNull(anyLong());
  }
}
