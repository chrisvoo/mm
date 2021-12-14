package utils;

import java.util.stream.IntStream;

/**
 * Conversion utility for boxing/unboxing types
 */
public class Conv {
  public static Byte[] byteToByte(byte[] nums) {
    if (nums == null) {
      return null;
    }

    return IntStream.range(0, nums.length)
      .mapToObj(i -> nums[i])
      .toArray(Byte[]::new);
  }

  public static byte[] ByteTobyte(Byte[] nums) {
    if (nums != null) {
      byte[] bytes = new byte[nums.length];
      int i=0;
      for(byte b: bytes)
        nums[i++] = b;

      return bytes;
    }

    return null;
  }
}