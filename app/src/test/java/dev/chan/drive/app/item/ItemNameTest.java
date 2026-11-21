package dev.chan.drive.app.item;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.text.Normalizer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ItemNameTest {

  @ParameterizedTest
  @NullSource
  @ValueSource(
      strings = {
        "test\\name",
        "test/name",
        "test:name",
        "test*name",
        "test?name",
        "test<name",
        "test>name",
        "test|name",
        "test\"name",
        ".",
        "..",
        "",
        " ",
        "\t",
        "\n"
      })
  void 금지된_이름이_주어지면_항목_이름_생성을_거부한다(String nameString) {
    // When / Then
    assertThatThrownBy(() -> ItemName.of(nameString)).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @ValueSource(strings = {"새폴더 ", " 새폴더"})
  void 앞뒤_공백과_NFD_문자열이_주어지면_공백을_제거하고_NFC로_정규화한다(String nameString) {
    // Given
    String nfdName = Normalizer.normalize(nameString, Normalizer.Form.NFD);

    // When
    ItemName output = ItemName.of(nfdName);

    // Then
    assertThat(output.getValue())
        .isEqualTo(Normalizer.normalize(nfdName.trim(), Normalizer.Form.NFC));
    assertThat(output.getValue()).isEqualTo("새폴더");
  }

  @Test
  void 파일_이름과_양수_인덱스가_주어지면_인덱스를_이름에_추가한다() {
    // Given / When
    ItemName newName = ItemName.of("memo.txt").withIndex(1);
    assertThat(newName.getValue()).isEqualTo("memo (1).txt");
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void 유효하지_않은_인덱스가_주어지면_이름_변경을_거부한다(int index) {
    // When / Then
    assertThatThrownBy(() -> ItemName.of("memo.txt").withIndex(index))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest(name = "[{index}] {0} -> hidden = {1}")
  @CsvSource({".gitignore, true", ".memo.json, true"})
  void 숨김_파일_이름이_주어지면_숨김_여부를_반환한다(String name, boolean expected) {
    // When / Then
    assertThat(ItemName.of(name).hidden()).isEqualTo(expected);
  }

  @ParameterizedTest(name = "{0} -> ext = {1}")
  @CsvSource({"memo.txt, txt", ".memo.json, json", ".gitignore, '',", "새폴더, ''"})
  void 파일_이름이_주어지면_확장자를_반환한다(String name, String expected) {
    // When / Then
    assertThat(ItemName.of(name).ext()).isEqualTo(expected);
  }
}
