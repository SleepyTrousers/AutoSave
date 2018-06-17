package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;

public class HandleString implements IHandler<String> {

  public HandleString() {
  }

  @Override
  public Class<?> getRootType() {
    return String.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, String object)
      throws IllegalArgumentException, IllegalAccessException {
    nbt.setString(name, object);
    return true;
  }

  @Override
  public @Nullable String read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable String object) {
    return nbt.hasKey(name) ? nbt.getString(name) : object;
  }

}
