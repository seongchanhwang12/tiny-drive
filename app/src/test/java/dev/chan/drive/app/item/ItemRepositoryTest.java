package dev.chan.drive.app.item;

import static org.assertj.core.api.Assertions.assertThat;

import dev.chan.drive.config.MysqlJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Set;

@MysqlJpaTest
public class ItemRepositoryTest {

  @Autowired ItemRepository itemRepository;
  @Autowired private TestEntityManager entityManager;

  @Test
  void 드라이브_ID와_루트_폴더가_저장되면_해당_루트_폴더를_조회한다() {
    // Given
    long driveId = 1L;
    ItemName itemName = ItemName.of("개인문서함");

    // When
    DriveItem root = new DriveItem(1L, null, itemName, CustomMimeTypes.FOLDER, 0L, 1L);

    itemRepository.save(root);
    DriveItem foundRoot = itemRepository.findByDriveIdAndParentIdIsNull(driveId).orElseThrow();

    // Then
    assertThat(foundRoot).isEqualTo(root);
    assertThat(foundRoot.getDriveId()).isEqualTo(driveId);
    assertThat(foundRoot.getParentId()).isNull();
    assertThat(foundRoot.getName()).isEqualTo(itemName);
  }

  @Test
  void 부모_ID가_주어지면_해당_폴더의_직접_자식_이름만_조회한다() {
    // Given
    long userId = 1L;

    DriveItem parentA = DriveItemFixture.folder(0L, "folder1", userId);

    itemRepository.save(parentA);

    DriveItem child1 =
        DriveItem.createUnder(parentA, ItemName.of("memo1.txt"), "text/plain", 10L, userId);

    DriveItem child2 =
        DriveItem.createUnder(parentA, ItemName.of("memo2.txt"), "text/plain", 10L, userId);

    itemRepository.saveAll(List.of(child1, child2));

    DriveItem parentB = DriveItemFixture.folder(999L, "folder2", userId);
    itemRepository.save(parentB);

    DriveItem other1 =
        DriveItem.createUnder(parentB, ItemName.of("other.txt"), "text/plain", 10L, userId);

    itemRepository.saveAll(List.of(parentB, other1));

    entityManager.flush();
    entityManager.clear();

    // When
    Set<ItemName> existingNames = itemRepository.findNamesByParentId(parentA.getId());

    // Then
    assertThat(existingNames)
        .extracting(ItemName::getValue)
        .containsExactlyInAnyOrder("memo1.txt", "memo2.txt");
  }
}
