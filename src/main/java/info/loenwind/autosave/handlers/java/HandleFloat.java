package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.TypeUtil;
import net.minecraft.nbt.NBTTagCompound;

public class HandleFloat implements IHandler<Float> {

  public HandleFloat() {
  }

  @Override
  public @Nullable IHandler<Float> getHandler(Type type) {
    return TypeUtil.isAssignable(Float.class, type) || TypeUtil.isAssignable(float.class, type) ? this : null;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, Float object)
      throws IllegalArgumentException, IllegalAccessException {
    nbt.setFloat(name, object);
    return true;
  }

  @Override
  public @Nullable Float read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable Float object) {
    return nbt.hasKey(name) ? nbt.getFloat(name) : object != null ? object : 0f;
  }

}
