package dev.chan.drive.app.item;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<DriveItem, Long>, ItemRepositoryCustom {
  Optional<DriveItem> findByDriveIdAndParentIdIsNull(Long driveId);
}
