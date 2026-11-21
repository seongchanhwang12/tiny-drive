package dev.chan.drive.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.chan.drive.config.MysqlJpaTest;

@MysqlJpaTest
class UserRepositoryTest {
  @Autowired UserRepository userRepository;

  @Test
  void 이메일을_가진_회원이_저장되면_해당_이메일의_존재를_확인한다() {
    // Given
    User user = User.builder().email("valid@email.com").pw("Password12!").build();

    // When
    User save = userRepository.save(user);
    boolean result = userRepository.existsByEmail("valid@email.com");

    // Then
    assertThat(result).isTrue();
    assertThat(save.getId()).isNotNull();
    assertThat(save.getEmail()).isEqualTo("valid@email.com");
    assertThat(save.getPw()).isEqualTo("Password12!");
  }
}
