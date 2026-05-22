package dev.chan.drive.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.print.DocFlavor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Drive {
  private static final long GB = 1024L * 1024L * 1024L;
  private static final long DEFAULT_QUOTA_BYTES = 50 * GB;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private long ownerId;
  private DriveType type;
  private long usedBytes;
  private long quotaBytes;

  public static Drive personal(long userId) {
    return new Drive(null, userId, DriveType.PERSONAL, 0L, DEFAULT_QUOTA_BYTES);
  }
}
