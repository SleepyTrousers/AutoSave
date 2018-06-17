package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.NullHelper;
import info.loenwind.autosave.util.TypeUtil;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings("rawtypes")
public abstract class HandleGenericType<T> implements IHandler<T> {
  
  @SuppressWarnings("unchecked")
  protected final List<IHandler>[] subHandlers = (List<IHandler>[]) new List[getRequiredParameters()];

  protected HandleGenericType(Registry registry, Type... parameterTypes) throws NoHandlerFoundException {
    if (parameterTypes.length == 0) {
      return;
    }
    if (parameterTypes.length != getRequiredParameters()) {
      throw new IllegalArgumentException("Mismatch of parameter count. Required: " + getRequiredParameters() + "  Found: " + parameterTypes.length);
    }
    for (int i = 0; i < getRequiredParameters(); i++) {
      Type type = parameterTypes[i];
      if (type == null) {
        throw new IllegalArgumentException("Null type passed to HandleGenericType()");
      }
      try {
        List<IHandler> handlers = registry.findHandlers(type);
        if (handlers.isEmpty()) {
          throw new NoHandlerFoundException(type, "Unknown");
        }
        subHandlers[i] = handlers;
      } catch (IllegalAccessException | InstantiationException e) {
        throw new NoHandlerFoundException(type, "Unknown", e);
      }
    }
  }
  
  @Override
  public abstract Class<?> getRootType();
  
  protected abstract IHandler<? extends T> create(Registry registry, Type... types) throws NoHandlerFoundException;
  
  protected int getRequiredParameters() {
    return getRootType().getTypeParameters().length;
  }
  
  protected boolean canHandle(Type type) {
    return TypeUtil.isAssignable(getRootType(), type);
  }
  
  @Override
  public @Nullable IHandler<? extends T> getHandler(Registry registry, Type type) {
    if (!canHandle(type)) {
      return null;
    }
    
    if (type instanceof ParameterizedType) {
      try {
        return create(registry, NullHelper.notnullJ(((ParameterizedType) type).getActualTypeArguments(), "ParameterizedType#getActualTypeArguments"));
      } catch (NoHandlerFoundException e) {
      } // Fallthrough to return null
    }

    return null;
  }
  
  @SuppressWarnings("unchecked")
  protected final void storeRecursive(int param, Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name, Object object) 
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    for (IHandler handler : subHandlers[param]) {
      if (handler.store(registry, phase, nbt, name, object)) {
        return;
      }
    }
  }

  @SuppressWarnings({ "unchecked"})
  protected final @Nullable <V> V readRecursive(int param, Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name, @Nullable V object) 
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    for (IHandler handler : subHandlers[param]) {
      V result = (V) handler.read(registry, phase, nbt, field, name, object);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}
