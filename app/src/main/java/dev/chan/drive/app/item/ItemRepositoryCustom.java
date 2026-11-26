package dev.chan.drive.app.item;

import java.util.Set;

public interface ItemRepositoryCustom {
  Set<ItemName> findNamesByParentId(long parentId);
}
