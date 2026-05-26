package dev.chan.drive.drive;

import dev.chan.drive.common.Common;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "drive",
    uniqueConstraints = {@UniqueConstraint(name = "uk_user_id", columnNames = "user_id")})
public class Drive extends Common {
  private static final long GB = 1024L * 1024L * 1024L;
  private static final long DEFAULT_QUOTA_BYTES = 50 * GB;

  @Column(nullable = false, unique = true)
  private long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private DriveType type;

  private long usedBytes;
  private long quotaBytes;

  public static Drive personal(long userId) {
    return new Drive(userId, DriveType.PERSONAL, 0L, DEFAULT_QUOTA_BYTES);
  }
}
