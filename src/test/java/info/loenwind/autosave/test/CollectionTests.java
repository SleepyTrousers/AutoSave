package info.loenwind.autosave.test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Registry;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.handlers.java.HandleEnum2EnumMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

public class CollectionTests {

  private static class Holder {

    public @Store List<String> strings;
    public @Store LinkedList<String> linkedListStrings;

    public @Store Set<String> stringSet;
    public @Store EnumSet<Direction> enumSet;
    
    public @Store Map<String, Integer> intMap;
    public @Store EnumMap<Direction, String> facingMap;
    public @Store EnumMap<Direction, Direction> facing2facing;
    
    public @Store Map<String, List<Map<Integer, EnumSet<Direction>>>> insanity;

    void fill() {
      strings = Lists.newArrayList("foo", "bar");
      linkedListStrings = Lists.newLinkedList(strings);
      
      intMap = new HashMap<>();
      intMap.put("foo", 123);
      intMap.put("bar", 456);
      
      stringSet = Sets.newHashSet("unique", "elements", "only");
      
      enumSet = EnumSet.of(Direction.UP, Direction.WEST, Direction.EAST);
      
      facingMap = new EnumMap<>(Direction.class);
      facingMap.put(Direction.UP, "up");
      facingMap.put(Direction.DOWN, "down");
      
      facing2facing = new EnumMap<>(Direction.class);
      facing2facing.put(Direction.UP, Direction.DOWN);
      facing2facing.put(Direction.EAST, Direction.WEST);
      
      Map<Integer, EnumSet<Direction>> innerMap = new HashMap<>();
      innerMap.put(42, EnumSet.of(Direction.NORTH, Direction.SOUTH));
      insanity = new HashMap<>();
      insanity.put("insane", Lists.newArrayList(innerMap));
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

  @SuppressWarnings("serial")
  @Test
  public void testEnum2EnumHandler() throws InstantiationException, IllegalAccessException {
    @SuppressWarnings("rawtypes")
    List<IHandler> handlers = Registry.GLOBAL_REGISTRY.findHandlers(new TypeToken<EnumMap<Direction, Direction>>(){}.getType());
    Assertions.assertTrue(handlers.size() == 2);
    Assertions.assertTrue(handlers.get(0) instanceof HandleEnum2EnumMap);
  }
  
  @Test
  public void testStringList() {
    Assertions.assertEquals(before.strings, after.strings);
  }
  
  @Test
  public void testStringLinkedList() {
    Assertions.assertEquals(before.linkedListStrings, before.linkedListStrings);
  }
  
  @Test
  public void testStringSet() {
    Assertions.assertEquals(before.stringSet, after.stringSet);
  }
  
  @Test
  public void testEnumSet() {
    Assertions.assertEquals(before.enumSet, after.enumSet);
  }

  @Test
  public void testMap() {
    Assertions.assertEquals(before.intMap, after.intMap);
  }
  
  @Test
  public void testEnumMap() {
    Assertions.assertEquals(before.facingMap, after.facingMap);
  }
  
  @Test
  public void testEnum2EnumMap() {
    Assertions.assertEquals(before.facing2facing, after.facing2facing);
  }
  
  @Test
  public void testNestedGenerics() {
    Assertions.assertEquals(before.insanity, after.insanity);
  }
}
