package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("rawtypes")
public class HandleCopyOnWriteArrayList extends HandleAbstractCollection<CopyOnWriteArrayList> {

  public HandleCopyOnWriteArrayList() throws NoHandlerFoundException {
    super();
  }
  
  protected HandleCopyOnWriteArrayList(Registry registry, Type[] types) throws NoHandlerFoundException {
    super(registry, types);
  }
  
  @Override
  public Class<?> getRootType() {
    return CopyOnWriteArrayList.class;
  }

  @Override
  protected @Nonnull CopyOnWriteArrayList makeCollection() {
    return new CopyOnWriteArrayList();
  }
  
  @Override
  protected IHandler<? extends CopyOnWriteArrayList> create(Registry registry, Type... types) throws NoHandlerFoundException {
    return new HandleCopyOnWriteArrayList(registry, types);
  }

  // TODO is this necessary?
  @Override
  public CopyOnWriteArrayList read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field,
      @Nonnull String name, @Nullable CopyOnWriteArrayList object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    final CopyOnWriteArrayList result = super.read(registry, phase, nbt, field, name, object);
    if (result != null) {
      while (result.remove(null)) {
      }
    }
    return result;
  }

}
