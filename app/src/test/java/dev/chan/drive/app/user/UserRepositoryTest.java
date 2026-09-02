package dev.chan.drive.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import dev.chan.drive.config.MySqlTestcontainersConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@Import(MySqlTestcontainersConfig.class)
class UserRepositoryTest {
  @Autowired UserRepository userRepository;

  @DisplayName("회원이 이미 존재하는지 테스트")
  @Test
  void exists_by_user_email() {
    // given
    User user = User.builder().email("valid@email.com").pw("Password12!").build();

    // when
    User save = userRepository.save(user);
    boolean result = userRepository.existsByEmail("valid@email.com");

    // then
    assertThat(result).isTrue();
    assertThat(save.getId()).isNotNull();
    assertThat(save.getEmail()).isEqualTo("valid@email.com");
    assertThat(save.getPw()).isEqualTo("Password12!");
  }
}
