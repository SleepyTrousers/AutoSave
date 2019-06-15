package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class ArrayTests {

  private static class Holder {

    public @Store byte[] bytes;
    public @Store Byte[] boxedBytes;

    public @Store int[]     ints;
    public @Store Integer[] boxedInts;

    public @Store short[] shorts;
    public @Store Short[] boxedShorts;

    public @Store char[]      chars;
    public @Store Character[] boxedChars;

    public @Store float[] floats;
    public @Store Float[] boxedFloats;

    public @Store long[] longs;
    public @Store Long[] boxedLongs;

    public @Store double[] doubles;
    public @Store Double[] boxedDoubles;

    public @Store String[] strings;
    public @Store Direction[] enums;

    void fill() {
      bytes = new byte[] { 1, -1, Byte.MAX_VALUE, Byte.MIN_VALUE };
      boxedBytes = ArrayUtils.toObject(bytes);

      ints = new int[] { 1, 2, 3, 4 };
      boxedInts = ArrayUtils.toObject(ints);

      shorts = new short[] { 1, -1, Short.MAX_VALUE, Short.MIN_VALUE };
      boxedShorts = ArrayUtils.toObject(shorts);

      chars = new char[] { 'a', '\n', Character.MAX_VALUE, Character.MIN_VALUE };
      boxedChars = ArrayUtils.toObject(chars);

      floats = new float[] { 0, (1f / 3f), Float.MAX_VALUE, Float.MIN_VALUE };
      boxedFloats = ArrayUtils.toObject(floats);

      longs = new long[] { 0, -1, Long.MAX_VALUE, Long.MIN_VALUE };
      boxedLongs = ArrayUtils.toObject(longs);

      doubles = new double[] { 0, (1f / 3f), Float.MAX_VALUE, Float.MIN_VALUE };
      boxedDoubles = ArrayUtils.toObject(doubles);

      strings = new String[] { "Recursive", "handlers", "are", "cool" };
      enums = new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH };
    }
  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
    // Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);

    before.fill();

    CompoundNBT tag = new CompoundNBT();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

  @Test
  public void testByteArray() {
    Assertions.assertArrayEquals(before.bytes, after.bytes);
  }

  @Test
  public void testBoxedByteArray() {
    Assertions.assertArrayEquals(before.boxedBytes, after.boxedBytes);
  }

  @Test
  public void testIntArray() {
    Assertions.assertArrayEquals(before.ints, after.ints);
  }

  @Test
  public void testBoxedIntArray() {
    Assertions.assertArrayEquals(before.boxedInts, after.boxedInts);
  }

  @Test
  public void testShortArray() {
    Assertions.assertArrayEquals(before.shorts, after.shorts);
  }

  @Test
  public void testBoxedShortArray() {
    Assertions.assertArrayEquals(before.boxedShorts, after.boxedShorts);
  }

  @Test
  public void testCharArray() {
    Assertions.assertArrayEquals(before.chars, after.chars);
  }

  @Test
  public void testBoxedCharArray() {
    Assertions.assertArrayEquals(before.boxedChars, after.boxedChars);
  }

  @Test
  public void testFloatArray() {
    Assertions.assertArrayEquals(before.floats, after.floats);
  }

  @Test
  public void testBoxedFloatArray() {
    Assertions.assertArrayEquals(before.boxedFloats, after.boxedFloats);
  }

  @Test
  public void testLongArray() {
    Assertions.assertArrayEquals(before.longs, after.longs);
  }

  @Test
  public void testBoxedLongArray() {
    Assertions.assertArrayEquals(before.boxedLongs, after.boxedLongs);
  }

  @Test
  public void testDoubleArray() {
    Assertions.assertArrayEquals(before.doubles, after.doubles);
  }

  @Test
  public void testBoxedDoubleArray() {
    Assertions.assertArrayEquals(before.boxedDoubles, after.boxedDoubles);
  }

  @Test
  public void testStringArray() {
    Assertions.assertArrayEquals(before.strings, after.strings);
  }
}
