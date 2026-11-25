package dev.chan.drive.app.item;

import static dev.chan.drive.app.item.CreateItemService.*;

import dev.chan.drive.app.auth.Principal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RestController
@RequiredArgsConstructor
public class ItemController {

  private final CreateItemService createItemService;

  @PostMapping("/items")
  public ResponseEntity<Object> create(
      @AuthenticationPrincipal final Principal principal,
      @RequestBody @Valid final CreateItemRequest request) {

    final CreateItemResponse response =
        CreateItemResponse.from(
            createItemService.create(
                principal.userId(),
                new Input(
                    request.parentId(),
                    request.fileName(),
                    request.mimeType(),
                    request.fileSize())));

    return ResponseEntity.ok().body(response);
  }
}
