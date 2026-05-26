package dev.chan.drive.drive;

import dev.chan.drive.app.drive.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRepository extends JpaRepository<Drive, Long> {}
