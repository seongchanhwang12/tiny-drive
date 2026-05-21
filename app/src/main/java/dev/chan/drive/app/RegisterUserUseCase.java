package dev.chan.drive.app;

import dev.chan.drive.domain.Drive;
import dev.chan.drive.domain.DuplicateEmailException;
import dev.chan.drive.domain.User;
import dev.chan.drive.repository.DriveRepository;
import dev.chan.drive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

  private final UserRepository userRepository;
  private final DriveRepository driveRepository;
  private final PasswordEncoder passwordEncoder;

  public record Input(String email, String pw) {}

  public record Output(long driveId, long userId, String email) {
    public static Output of(User user, Drive drive) {
      return new Output(drive.getId(), user.getId(), user.getEmail());
    }
  }

  @Transactional
  public Output execute(Input input) {
    if (userRepository.existsByEmail(input.email())) {
      throw new DuplicateEmailException("Email already exists");
    }

    String encodedPw = passwordEncoder.encode(input.pw);
    User newUser = User.register(input.email(), encodedPw);
    User savedUser = userRepository.save(newUser);

    Drive personal = Drive.personal(savedUser.getId());
    Drive savedDrive = driveRepository.save(personal);

    return Output.of(savedUser, savedDrive);
  }
}
