package dev.chan.drive.app.common;

public final class Guard {

  private Guard() {
    throw new AssertionError("No Guard instances");
  }

  public static <T> T notNull(T value) {
    if (value == null) {
      throw new IllegalArgumentException("Value must not be null");
    }

    return value;
  }

  public static String notBlank(String value, String name) {
    notNull(value);
    if (value.isBlank()) {
      throw new IllegalArgumentException(name + " must not be blank. value=" + value);
    }

    return value;
  }

  public static long positive(long value, String name) {
    if (value <= 0) {
      throw new IllegalArgumentException(name + " must be positive. value=" + value);
    }

    return value;
  }

  public static long positiveOrZero(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("Value must be positive or zero. value=" + value);
    }

    return value;
  }
}
