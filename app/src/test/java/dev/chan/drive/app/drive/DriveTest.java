package dev.chan.drive.app.drive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DriveTest {

  @DisplayName("개인 문서함 생성 시 사용량은 0으로, 총 사용가능 용량은 50GB로 초기화된다")
  @Test
  void initialize_usage_when_create_personal_drive() {
    // given
    Drive personalDrive = Drive.personal(1L);

    // when
    assertThat(personalDrive.getUsedBytes()).isZero();
    assertThat(personalDrive.getQuotaBytes()).isEqualTo(1024L * 1024 * 1024 * 50);
  }
}
