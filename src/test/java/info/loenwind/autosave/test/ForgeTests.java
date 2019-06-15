package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class ForgeTests {
  
  private static class Holder {

    // Simple registry objects
    public @Store Block block;
    public @Store Item item;
    public @Store Enchantment ench;

    // Verify registry objects work with subtypes
    public @Store ChestBlock chest;
    public @Store BowItem bow;
    public @Store DamageEnchantment sharpness;

    // Other forge classes
//    public @Store Fluid fluid;
//    public @Store FluidStack stack;

    void fill() {
      block = Blocks.BEDROCK;
      item = Items.APPLE;
      ench = Enchantments.PROTECTION;

      chest = (ChestBlock) Blocks.CHEST;
      bow = (BowItem) Items.BOW;
      sharpness = (DamageEnchantment) Enchantments.SHARPNESS;
      
//      fluid = Fluid.LAVA;
//      stack = new FluidStack(FluidRegistry.WATER, 534);
    }
  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() throws Exception {
    // Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);
    before.fill();

    CompoundNBT tag = new CompoundNBT();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }
  
  @Test
  public void testUnregistered() {
    class Unregistered {
      @Store public Block block = new Block(Block.Properties.create(Material.AIR));
    }
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new Unregistered()));
  }

  @Test
  public void testBlock() {
    Assertions.assertSame(before.block, after.block);
  }

  @Test
  public void testItem() {
    Assertions.assertSame(before.item, after.item);
  }
  
  @Test
  public void testEnchantment() {
    Assertions.assertSame(before.ench, after.ench);
  }
  
  @Test
  public void testBlockSubclass() {
    Assertions.assertSame(before.chest, after.chest);
  }
  
  @Test
  public void testItemSubclass() {
    Assertions.assertSame(before.bow, after.bow);
  }
  
  @Test
  public void testEnchantmentSubclass() {
    Assertions.assertSame(before.sharpness, after.sharpness);
  }
  /*
  @Test
  public void testFluid() {
    Assertions.assertNotNull(after.fluid);
    Assertions.assertSame(before.fluid, after.fluid);
  }

  @Test
  public void testFluidStack() {
    Assertions.assertNotNull(after.stack);
    Assertions.assertTrue(before.stack.isFluidStackIdentical(after.stack));
  }
  */
}