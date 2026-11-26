package dev.chan.drive.app.drive;

import dev.chan.drive.app.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "drive",
    uniqueConstraints = {@UniqueConstraint(name = "uk_owner_id", columnNames = "owner_id")})
public class Drive extends BaseEntity {
  private static final long GB = 1024L * 1024L * 1024L;
  private static final long DEFAULT_QUOTA_BYTES = 50 * GB;

  @Column(nullable = false, unique = true)
  private long ownerId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private DriveType type;

  private long usedBytes;
  private long quotaBytes;

  public static Drive personal(long ownerId) {
    return new Drive(ownerId, DriveType.PERSONAL, 0L, DEFAULT_QUOTA_BYTES);
  }
}
