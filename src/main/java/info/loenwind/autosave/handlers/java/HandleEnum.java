package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class HandleEnum implements IHandler<Enum<?>> {

  public HandleEnum() {
  }

  @Override
  public Class<?> getRootType() {
    return Enum.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name,
      Enum<?> object) throws IllegalArgumentException, IllegalAccessException {
    nbt.setInteger(name, object.ordinal());
    return true;
  }

  @Override
  public @Nullable Enum<?> read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable Enum<?> object) {
    if (nbt.hasKey(name)) {
      Enum<?>[] enumConstants = null;
      if (object != null) {
        enumConstants = object.getClass().getEnumConstants();
      } else if (field != null) {
        enumConstants = (Enum<?>[]) field.getType().getEnumConstants();
      }
      if (enumConstants != null) {
        return enumConstants[MathHelper.clamp(nbt.getInteger(name), 0, enumConstants.length - 1)];
      }
    }
    return object;
  }

}
