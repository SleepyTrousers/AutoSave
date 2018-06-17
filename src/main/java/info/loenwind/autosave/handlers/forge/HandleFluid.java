package info.loenwind.autosave.handlers.forge;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.NullHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class HandleFluid implements IHandler<Fluid> {

  public HandleFluid() {
  }

  @Override
  public Class<?> getRootType() {
    return Fluid.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, Fluid object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    nbt.setString(name, NullHelper.notnullF(FluidRegistry.getFluidName(object), "Fluid " + object + " is missing from fluid registry"));
    return true;
  }

  @Override
  public @Nullable Fluid read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable Fluid object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return nbt.hasKey(name) ? FluidRegistry.getFluid(nbt.getString(name)) : object;
  }

}
