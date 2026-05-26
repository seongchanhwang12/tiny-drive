package dev.chan.drive.user;

import dev.chan.drive.drive.Drive;
import dev.chan.drive.common.errors.RestApiException;
import dev.chan.drive.common.errors.CustomErrorCode;
import dev.chan.drive.drive.DriveRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

  private final UserRepository userRepository;
  private final DriveRepository driveRepository;
  private final PasswordEncoder passwordEncoder;
  private final PasswordPolicy passwordPolicy;

  public record Input(
      @NotBlank(message = "Email is required") @Email(message = "Invalid email format")
          String email,
      @NotBlank(message = "Password is required") String pw) {}

  public record Output(long driveId, long userId, String email) {
    public static Output of(User user, Drive drive) {
      return new Output(drive.getId(), user.getId(), user.getEmail());
    }
  }

  @Transactional
  public Output execute(final Input input) {
    log.info("User signup requested. email={}", input.email);
    final String rawPassword = input.pw;
    final String email = input.email;

    if (userRepository.existsByEmail(email)) {
      log.warn("User signup rejected. reason=duplicate_email email={}", email);
      throw new RestApiException(CustomErrorCode.DUPLICATE_EMAIL);
    }

    passwordPolicy.validate(rawPassword);

    final String encodedPassword = passwordEncoder.encode(rawPassword);
    final User newUser = User.register(email, encodedPassword);
    final User savedUser = userRepository.save(newUser);

    final Drive personal = Drive.personal(savedUser.getId());
    final Drive savedDrive = driveRepository.save(personal);

    log.info(
        "User signup completed. userId={} driveId={} email={}",
        savedUser.getId(),
        savedDrive.getId(),
        savedUser.getEmail());

    return Output.of(savedUser, savedDrive);
  }
}
