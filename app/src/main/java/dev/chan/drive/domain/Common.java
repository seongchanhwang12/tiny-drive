package dev.chan.drive.domain;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class Common {
  @CreatedDate private LocalDateTime createdAt;
  @LastModifiedDate private LocalDateTime lastModifiedAt;
  @CreatedBy private Long createdBy;
  @LastModifiedBy private Long lastModifiedBy;
}
