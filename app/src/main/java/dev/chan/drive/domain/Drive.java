package dev.chan.drive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.print.DocFlavor;

@Getter
@Builder
@AllArgsConstructor
public class Drive {
  private static final long GB = 1024L * 1024L * 1024L;
  private static final long DEFAULT_QUOTA_BYTES = 50 * GB;

  private Long id;
  private long ownerId;
  private DriveType type;
  private long usedBytes;
  private long quotaBytes;

  public static Drive personal(long userId) {
    return new Drive(null, userId, DriveType.PERSONAL, 0L, DEFAULT_QUOTA_BYTES);
  }
}
