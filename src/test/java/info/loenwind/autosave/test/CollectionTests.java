package info.loenwind.autosave.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.util.Log;
import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;

public class CollectionTests {

  private static class Holder {

    public @Store List<String> strings;
    public @Store Map<String, Integer> intMap;

    void fill() {
      strings = Lists.newArrayList("foo", "bar");
      intMap = new HashMap<>();
      intMap.put("foo", 123);
      intMap.put("bar", 456);
    }

  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
//    Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);
    Bootstrap.register();

    before.fill();

    NBTTagCompound tag = new NBTTagCompound();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

  @Test
  public void testStringList() {
    Assertions.assertEquals(before.strings, after.strings);
  }

  @Test
  public void testMap() {
    Assertions.assertEquals(before.intMap, after.intMap);
  }
}
