package info.loenwind.autosave.test;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.init.Bootstrap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ForgeTests {
  
  private static class Holder {

    public @Store Fluid fluid;
    public @Store FluidStack stack;

    void fill() {
      fluid = FluidRegistry.LAVA;
      stack = new FluidStack(FluidRegistry.WATER, 534);
    }
  }

  private static @Nonnull Holder before = new Holder(), after = new Holder();

  @BeforeAll
  public static void setup() {
    // Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);
    Bootstrap.register();
    
    before.fill();

    NBTTagCompound tag = new NBTTagCompound();
    Writer.write(tag, before);
    Reader.read(tag, after);
  }

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
}