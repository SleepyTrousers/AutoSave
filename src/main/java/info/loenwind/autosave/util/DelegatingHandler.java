package info.loenwind.autosave.util;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

public class DelegatingHandler<T, R> implements IHandler<T> {
  
  private final @Nonnull Class<T> clazz;
  private final @Nonnull IHandler<R> delegate;
  private final @Nonnull Function<T, R> storeConverter;
  private final @Nonnull Function<R, T> readConverter;
  
  public DelegatingHandler(Class<T> clazz, IHandler<R> delegate, Function<T, R> storeConverter, Function<R, T> readConverter) {
    this.clazz = clazz;
    this.delegate = delegate;
    this.storeConverter = storeConverter;
    this.readConverter = readConverter;
  }
  
  @Override
  public Class<?> getRootType() {
    return clazz;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, T object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    R obj = storeConverter.apply(object);
    if (obj == null) {
      throw new IllegalArgumentException("Store converter returned null unexpectedly.");
    }
    return delegate.store(registry, phase, nbt, name, obj);
  }
  
  @Override
  @Nullable
  public T read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name, @Nullable T object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return readConverter.apply(delegate.read(registry, phase, nbt, field, name, storeConverter.apply(object)));
  }
}
