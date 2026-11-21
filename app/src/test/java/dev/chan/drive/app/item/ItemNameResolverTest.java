package dev.chan.drive.app.item;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemNameResolverTest {
  @InjectMocks ItemNameResolver sut;
  @Test
  void 기존_이름과_중복되지_않으면_원래_이름을_유지한다() {
    // Given
    ItemName originName = ItemName.of("memo.txt");
    Set<ItemName> existingNames = Set.of(ItemName.of("file"));

    // When
    ItemName suggestedName = sut.resolve(originName, existingNames);

    // Then
    assertThat(suggestedName).isEqualTo(originName);
  }

  @Test
  void 이름과_연속된_중복_번호가_주어지면_다음_번호를_부여한다() {
    // Given
    ItemName originName = ItemName.of("memo.txt");
    Set<ItemName> existingNames =
        Set.of(ItemName.of("memo.txt"), ItemName.of("memo (1).txt"), ItemName.of("memo (2).txt"));

    // When
    ItemName uniqueName = sut.resolve(originName, existingNames);

    // Then
    assertThat(uniqueName.getValue()).isEqualTo("memo (3).txt");
  }
}
