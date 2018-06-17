package info.loenwind.autosave.handlers.internal;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A dummy {@link IHandler} that is used as default value for fields. It will be
 * ignored and the fields' handlers will be looked up in the {@link Registry}
 * instead.
 * <p>
 * This is needed because Java annotations do not allow "null" as a default
 * value for class parameters.
 * <p>
 * Do not add this handler to an annotation.
 */
public class NullHandler implements IHandler<NullHandler> {

  private NullHandler() {
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name,
      NullHandler object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return false;
  }

  @Override
  public @Nullable NullHandler read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field,
      String name, @Nullable NullHandler object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return null;
  }

}
