package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class HandleAbstractCollection<T extends Collection> extends HandleGenericType<T> {
  
  public HandleAbstractCollection() throws NoHandlerFoundException { 
    this(new Type[0]); 
  }

  protected HandleAbstractCollection(Type... types) throws NoHandlerFoundException {
    super(types);
  }

  @Override
  public Class<?> getRootType() {
    return Collection.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, T object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("size", object.size());
    int i = 0;
    for (Object elem : object) {
      if (elem != null) {
        for (IHandler handler : subHandlers[0]) {
          handler.store(registry, phase, tag, i + "", elem);
        }
      }
      i++;
    }
    nbt.setTag(name, tag);
    return true;
  }

  @Override
  public @Nullable T read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable T object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      if (object == null) {
        object = makeCollection();
      } else {
        object.clear();
      }

      NBTTagCompound tag = nbt.getCompoundTag(name);
      int size = tag.getInteger("size");
      for (int i = 0; i < size; i++) {
        if (tag.hasKey(i + "")) {
          object.add(readRecursive(0, registry, phase, tag, null, i + "", null));
        } else {
//          object.add(makeEmptyValueObject()); // TODO
        }
      }
    }
    return object;
  }

  abstract protected T makeCollection();
}
