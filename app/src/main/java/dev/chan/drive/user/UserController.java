package dev.chan.drive.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final RegisterUserUseCase registerUserUseCase;

  @PostMapping("/users")
  public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserUseCase.Input input) {
    return ResponseEntity.status(HttpStatus.CREATED).body(registerUserUseCase.execute(input));
  }
}
