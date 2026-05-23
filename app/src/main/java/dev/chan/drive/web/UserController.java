package dev.chan.drive.web;

import dev.chan.drive.app.RegisterUserUseCase;
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
public class RegisterUserController {
  private final RegisterUserUseCase registerUserUseCase;

  @PostMapping("/users")
  public ResponseEntity<?> registerUser(@RequestBody RegisterUserUseCase.Input input) {
    RegisterUserUseCase.Output output = registerUserUseCase.execute(input);
    return ResponseEntity.status(HttpStatus.CREATED).body(output);
  }
}
