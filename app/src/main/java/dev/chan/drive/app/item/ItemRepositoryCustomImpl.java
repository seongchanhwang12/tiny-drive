package dev.chan.drive.app.item;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Set<ItemName> findNamesByParentId(long parentId) {
    QDriveItem item = QDriveItem.driveItem;

    return queryFactory
        .select(item.name.value)
        .from(item)
        .where(item.parentId.eq(parentId))
        .fetch()
        .stream()
        .map(ItemName::of)
        .collect(Collectors.toCollection(HashSet::new));
  }
}
