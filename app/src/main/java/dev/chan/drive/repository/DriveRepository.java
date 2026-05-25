package dev.chan.drive.repository;

import dev.chan.drive.domain.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRepository extends JpaRepository<Drive,Long> {
}
