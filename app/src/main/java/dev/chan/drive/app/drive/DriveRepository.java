package dev.chan.drive.app.drive;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, Long> {
  Optional<Drive> findByOwnerId(long ownerId);
}
