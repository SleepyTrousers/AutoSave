package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.util.Log;
import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class JavaTests {

  private static class Holder {

    public @Store boolean    bool;
    public @Store float      floaty;
    public @Store float[]    floaties = new float[4]; // Handler will not create a new array
    public @Store int        integer;
    public @Store String     string;
    public @Store EnumFacing facing;

    void fill() {
      bool = true;
      floaty = 3.141569F;
      floaties = new float[] { 1, 0.5F, 0.25F, 0.125F };
      integer = 42;
      string = "Hello World!";
      facing = EnumFacing.WEST;
    }

  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
    Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);
    Bootstrap.register();

    before.fill();

    NBTTagCompound tag = new NBTTagCompound();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

  @Test
  public void testBoolean() {
    Assert.assertEquals(before.bool, after.bool);
  }

  @Test
  public void testFloat() {
    Assert.assertEquals(before.floaty, after.floaty, 0.01F);
  }

  @Test
  public void testFloatArray() {
    Assert.assertNotNull(after.floaties);
    Assert.assertArrayEquals(before.floaties, after.floaties, 0.01F);
  }

  @Test
  public void testInteger() {
    Assert.assertEquals(before.integer, after.integer);
  }

  @Test
  public void testString() {
    Assert.assertNotNull(after.string);
    Assert.assertEquals(before.string, after.string);
  }

  @Test
  public void testEnum() {
    Assert.assertNotNull(after.facing);
    Assert.assertEquals(before.facing, after.facing);
  }

}
