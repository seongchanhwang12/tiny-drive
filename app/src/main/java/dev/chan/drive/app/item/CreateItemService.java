package dev.chan.drive.app.item;

import dev.chan.drive.app.storage.PresignedUpload;
import dev.chan.drive.app.storage.StoragePort;
import dev.chan.drive.app.storage.UploadProperties;
import dev.chan.drive.error.ApiException;
import dev.chan.drive.error.CustomErrorCode;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateItemService {

  public record Input(Long parentId, String fileName, String mimeType, long size) {}

  public record Output(DriveItem item, PresignedUpload upload) {}

  private final ItemRepository itemRepository;
  private final StoragePort storagePort;
  private final ItemNameResolver itemNameResolver;
  private final UploadProperties uploadProperties;
  private final ParentFolderResolver parentFolderResolver;

  @Transactional
  public Output create(final long userId, final Input command) {
    validateFileSize(command.size());

    final ItemName itemName = ItemName.of(command.fileName());
    final DriveItem parentFolder = parentFolderResolver.resolve(command.parentId(), userId);

    final Set<ItemName> itemNames = itemRepository.findNamesByParentId(parentFolder.getId());

    final ItemName suggestedName = itemNameResolver.resolve(itemName, itemNames);

    final DriveItem newItem =
        DriveItem.createUnder(
            parentFolder, suggestedName, command.mimeType(), command.size(), userId);
    final DriveItem saved = itemRepository.save(newItem);

    if (newItem.isFolder()) {
      return new Output(saved, null);
    }

    final String fileKey = UUID.randomUUID().toString();
    final PresignedUpload sign = storagePort.createPutUrl(fileKey, saved.getMimeType());

    return new Output(saved, sign);
  }

  private void validateFileSize(final long size) {
    if (size > uploadProperties.maxFileSize().toBytes()) {
      throw new ApiException(
          CustomErrorCode.FILE_SIZE_LIMIT_EXCEEDED,
          "max file size = " + uploadProperties.maxFileSize());
    }
  }
}
