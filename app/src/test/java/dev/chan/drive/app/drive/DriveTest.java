package dev.chan.drive.app.drive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DriveTest {

  @Test
  void 개인_문서함이_생성되면_사용량은_0이고_사용가능_용량은_50GB로_초기화된다() {
    // When
    Drive personalDrive = Drive.personal(1L);

    // Then
    assertThat(personalDrive.getUsedBytes()).isZero();
    assertThat(personalDrive.getQuotaBytes()).isEqualTo(1024L * 1024 * 1024 * 50);
  }
}
