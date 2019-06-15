package info.loenwind.autosave.test;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LogBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MinecraftTests {

  private static class Holder {

    public @Store BlockPos pos;
    public @Store BlockState state;
    public @Store ItemStack stack;
    public @Store ResourceLocation resloc;
    
    // Verify arrays/collections work with MC objects
    public @Store ItemStack[] stackArray;
    public @Store List<ItemStack> stackList;

    void fill() {
      pos = new BlockPos(867, 5, 309);
      state = Blocks.OAK_LOG.getDefaultState().with(LogBlock.AXIS, Direction.Axis.Z);
      stack = new ItemStack(Items.GOLDEN_APPLE, 32);
      resloc = new ResourceLocation("fancy", "strings");
      
      stackArray = new ItemStack[] { new ItemStack(Items.BEEF), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.MUTTON) };
      stackList = Arrays.asList(stackArray);
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
  public void testBlockPos() {
    Assertions.assertEquals(before.pos, after.pos);
  }

  @Test
  public void testBlockState() {
    Assertions.assertSame(before.state, after.state);
  }
  
  @Test
  public void testResourceLocation() {
    Assertions.assertEquals(before.resloc, after.resloc);
  }
  
  private boolean compareStacks(ItemStack i1, ItemStack i2) {
    return i1 == i2 || (i1 != null && i2 != null && ItemStack.areItemsEqual(i1, i2) && ItemStack.areItemStackTagsEqual(i1, i2));
  }

  @Test
  public void testItemStack() {
    Assertions.assertTrue(compareStacks(before.stack, after.stack));
  }

  @Test
  public void testItemStackArray() {
    Assertions.assertEquals(before.stackArray.length, after.stackArray.length);
    for (int i = 0; i < before.stackArray.length; i++) {
      Assertions.assertTrue(compareStacks(before.stackArray[i], after.stackArray[i]));
    }
  }

  @Test
  public void testItemStackList() {
    Assertions.assertEquals(before.stackList.size(), after.stackList.size());
    for (int i = 0; i < before.stackList.size(); i++) {
      Assertions.assertTrue(compareStacks(before.stackList.get(i), after.stackList.get(i)));
    }
  }
}
