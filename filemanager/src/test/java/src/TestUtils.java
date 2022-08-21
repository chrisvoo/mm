package src;

import com.google.inject.Guice;

import java.util.Random;

public class TestUtils {
  public static String randomString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
      .limit(targetStringLength)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }

  public static <T> T inject(Class<T> theClass) {
    return Guice.createInjector(new TestModule()).getInstance(theClass);
  }
}