package dev.chan.drive.app.item;

import dev.chan.drive.app.common.Guard;
import java.text.Normalizer;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ItemName {

  private static final Set<Character> FORBIDDEN =
      Set.of('\\', '/', ':', '*', '?', '"', '<', '>', '|');

  @Column(nullable = false, name = "ITEM_NAME")
  private String value;

  /**
   * 앞뒤 공백을 제거하고 NFC로 정규화한다. Windows 호환성을 위해 끝의 점을 금지한다.
   *
   * @param value
   * @return
   */
  public static ItemName of(final String value) {
    String itemName = Guard.notBlank(value, "itemName").trim();
    String normalizedName = Normalizer.normalize(itemName, Normalizer.Form.NFC);

    if (normalizedName.endsWith("."))
      throw new IllegalArgumentException(
          "Value contains invalid characters. ItemName cant not have ends with dot");

    if (normalizedName.chars().anyMatch(c -> FORBIDDEN.contains((char) c)))
      throw new IllegalArgumentException(
          "Value contains invalid characters. ItemName cant not have Forbidden Characters = ['\\\\', '/', ':', '*', '?', '\"', '<', '>', '|' ]");

    return new ItemName(normalizedName);
  }

  public ItemName withIndex(int index) {
    Guard.positive(index, "item name index");
    return ItemName.of(base() + " (" + index + ")" + extWithDot());
  }

  public boolean hidden() {
    return value.indexOf('.') == 0 && value.length() > 1;
  }

  public String base() {
    return hasExt() ? value.substring(0, extIndex()) : value;
  }

  /** 점을 포함한 확장자를 반환하며, 확장자가 없으면 빈 문자열을 반환한다. */
  public String extWithDot() {
    return hasExt() ? value.substring(extIndex()) : "";
  }

  /** 점을 제외한 확장자를 반환하며, 확장자가 없으면 빈 문자열을 반환한다. */
  public String ext() {
    return hasExt() ? value.substring(extIndex() + 1) : "";
  }

  /**
   * 마지막 점 뒤를 확장자로 취급한다. 단, 이름 맨 앞의 점은 확장자 구분자로 취급하지 않는다. 예: ".gitignore"는 확장자가 없고, ".memo.json"의
   * 확장자는 "json"이다.
   */
  private boolean hasExt() {
    int dot = extIndex();
    return dot > 0 && dot < value.length() - 1;
  }

  /** 확장자의 시작 인덱스를 반환한다. */
  private int extIndex() {
    return value.lastIndexOf('.');
  }
}
