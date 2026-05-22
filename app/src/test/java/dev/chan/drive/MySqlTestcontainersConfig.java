package dev.chan.drive;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class MySqlTestcontainersConfig {

  @Bean
  @ServiceConnection
  static MySQLContainer<?> mySqlContainer() {
    return new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
        .withDatabaseName("drive-test")
        .withUsername("test")
        .withPassword("test");
  }
}
