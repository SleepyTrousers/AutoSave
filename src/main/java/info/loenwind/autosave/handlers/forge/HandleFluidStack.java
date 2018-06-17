package info.loenwind.autosave.handlers.forge;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class HandleFluidStack implements IHandler<FluidStack> {

  public HandleFluidStack() {
  }

  @Override
  public Class<?> getRootType() {
    return FluidStack.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, FluidStack object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    NBTTagCompound tag = new NBTTagCompound();
    object.writeToNBT(tag);
    nbt.setTag(name, tag);
    return false;
  }

  @Override
  public @Nullable FluidStack read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable FluidStack object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(name));
    }
    return null;
  }

}
