package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class JavaTests {

  private static class Holder {

    public @Store boolean bool;
    public @Store char    character;
    public @Store byte    bite;
    public @Store short   shorty;
    public @Store int     integer;
    public @Store long    longy;
    public @Store float   floaty;
    public @Store double  doubly;

    public @Store String     string;
    public @Store Direction facing;

    void fill() {
      bool = true;
      character = 'q';
      bite = 127;
      shorty = 1098;
      integer = 42;
      longy = 999_999_999_999L;
      floaty = 3.141569F;
      doubly = Math.PI * 2;
      
      string = "Hello World!";
      facing = Direction.WEST;
    }

  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
//    Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);

    before.fill();

    CompoundNBT tag = new CompoundNBT();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

  @Test
  public void testBoolean() {
    Assertions.assertEquals(before.bool, after.bool);
  }
  
  @Test
  public void testChar() {
    Assertions.assertEquals(before.character, after.character);
  }

  @Test
  public void testByte() {
    Assertions.assertEquals(before.bite, after.bite);
  }

  @Test
  public void testShort() {
    Assertions.assertEquals(before.shorty, after.shorty);
  }

  @Test
  public void testInteger() {
    Assertions.assertEquals(before.integer, after.integer);
  }

  @Test
  public void testLong() {
    Assertions.assertEquals(before.longy, after.longy);
  }

  @Test
  public void testFloat() {
    Assertions.assertEquals(before.floaty, after.floaty, 0.01F);
  }

  @Test
  public void testDouble() {
    Assertions.assertEquals(before.doubly, after.doubly);
  }
  
  @Test
  public void testString() {
    Assertions.assertNotNull(after.string);
    Assertions.assertEquals(before.string, after.string);
  }

  @Test
  public void testEnum() {
    Assertions.assertNotNull(after.facing);
    Assertions.assertEquals(before.facing, after.facing);
  }

}
