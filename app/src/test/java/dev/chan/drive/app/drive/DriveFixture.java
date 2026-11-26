package dev.chan.drive.app.drive;

import org.springframework.test.util.ReflectionTestUtils;

public class DriveFixture {

  public static Drive personalDrive(long userId) {
    Drive personal = Drive.personal(userId);
    ReflectionTestUtils.setField(personal, "id", 1L);
    return personal;
  }
}
