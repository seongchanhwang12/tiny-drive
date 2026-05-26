package dev.chan.drive.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import dev.chan.drive.app.drive.Drive;
import dev.chan.drive.app.drive.DriveType;
import dev.chan.drive.app.user.PasswordPolicy;
import dev.chan.drive.app.user.RegisterUserUseCase;
import dev.chan.drive.app.user.User;
import dev.chan.drive.app.user.UserRepository;
import dev.chan.drive.error.RestApiException;
import dev.chan.drive.error.CustomErrorCode;
import dev.chan.drive.app.drive.DriveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("회원가입 유스케이스")
@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

  private static final Long USER_ID = 1L;
  private static final Long DRIVE_ID = 10L;
  private static final String VALID_EMAIL = "test@example.com";
  private static final String VALID_PASSWORD = "Password123!";
  private static final String ENCODED_PASSWORD = "Encoded-password";

  @InjectMocks RegisterUserUseCase registerUserUseCase;

  @Mock UserRepository userRepository;

  @Mock DriveRepository driveRepository;

  @Mock PasswordEncoder passwordEncoder;

  @Mock PasswordPolicy passwordPolicy;

  @DisplayName("전달받은 값이 유효하면 회원가입에 성공하고 개인문서함 생성 요청을 수행한다")
  @Test
  void register_user_success() {
    // given
    RegisterUserUseCase.Input input = validInput();

    given(passwordEncoder.encode(any())).willReturn(ENCODED_PASSWORD);
    given(userRepository.existsByEmail(input.email())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(savedUser());
    given(driveRepository.save(any(Drive.class))).willReturn(savedPersonalDrive());

    // when
    RegisterUserUseCase.Output output = registerUserUseCase.execute(input);

    // then
    assertThat(output.userId()).isEqualTo(USER_ID);
    assertThat(output.email()).isEqualTo(VALID_EMAIL);
    assertThat(output.driveId()).isEqualTo(DRIVE_ID);

    then(userRepository).should().existsByEmail(VALID_EMAIL);
    then(passwordPolicy).should().validate(VALID_PASSWORD);
    then(passwordEncoder).should().encode(VALID_PASSWORD);
    then(userRepository).should().save(any(User.class));
    then(driveRepository).should().save(any(Drive.class));
  }

  @DisplayName("이미 존재하는 이메일이면 회원가입에 실패하고 개인문서함을 생성하지 않는다")
  @Test
  void fail_when_email_already_exists() {
    // given
    final RegisterUserUseCase.Input input = validInput();

    given(userRepository.existsByEmail(input.email())).willReturn(true);

    // when & then
    final RestApiException result =
        assertThrows(RestApiException.class, () -> registerUserUseCase.execute(input));

    assertThat(result.getErrorCode()).isEqualTo(CustomErrorCode.DUPLICATE_EMAIL);

    then(userRepository).should().existsByEmail(VALID_EMAIL);
    then(passwordEncoder).should(never()).encode(VALID_PASSWORD);
    then(userRepository).should(never()).save(any(User.class));
    then(driveRepository).should(never()).save(any(Drive.class));
  }

  private RegisterUserUseCase.Input validInput() {
    return new RegisterUserUseCase.Input(VALID_EMAIL, VALID_PASSWORD);
  }

  private User savedUser() {
    User user = User.register(VALID_EMAIL, VALID_PASSWORD);
    ReflectionTestUtils.setField(user, "id", 1L);
    return user;
  }

  private Drive savedPersonalDrive() {
    Drive drive = Drive.builder().userId(USER_ID).type(DriveType.PERSONAL).build();
    ReflectionTestUtils.setField(drive, "id", 10L);
    return drive;
  }
}
