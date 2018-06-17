package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.nbt.NBTTagCompound;

public class ArrayTests {

  private static class Holder {

    public @Store int[]    ints;
    public @Store float[]  floats;
    public @Store String[] strings;

    void fill() {
      ints = new int[] { 1, 2, 3, 4 };
      floats = new float[] { 1, 0.5f, 0.25f, 0.125f };
      strings = new String[] { "Recursive", "handlers", "are", "cool" };
    }
  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
    // Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);

    before.fill();

    NBTTagCompound tag = new NBTTagCompound();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

  @Test
  public void testIntArray() {
    Assertions.assertArrayEquals(before.ints, after.ints);
  }

  @Test
  public void testFloatArray() {
    Assertions.assertArrayEquals(before.floats, after.floats);
  }

  @Test
  public void testStringArray() {
    Assertions.assertArrayEquals(before.strings, after.strings);
  }
}
