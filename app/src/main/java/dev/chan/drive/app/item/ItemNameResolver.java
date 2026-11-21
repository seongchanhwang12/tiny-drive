package dev.chan.drive.app.item;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ItemNameResolver {

  public ItemName resolve(ItemName itemName, Set<ItemName> existingNames) {
    if (!existingNames.contains(itemName)) return itemName;
    int index = 1;
    ItemName candidate;

    do {
      candidate = itemName.withIndex(index++);
    } while (existingNames.contains(candidate));

    return candidate;
  }
}
