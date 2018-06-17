package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.NonnullType;
import info.loenwind.autosave.util.TypeUtil;
import net.minecraft.nbt.NBTTagCompound;

public class HandlePrimitive<T> implements IHandler<T> {
  
  public interface WriterFunc<@NonnullType T> {
    
    void set(NBTTagCompound tag, String name, @Nullable T object);
    
  }
  
  public interface ReaderFunc<@NonnullType T> {
    
    T get(NBTTagCompound tag, String name);
    
  }
  
  private final T defaultValue;
  
  private final @Nullable Class<?> primitiveClass;
  private final Class<?> boxedClass;
  
  private final WriterFunc<T> writer;
  private final ReaderFunc<T> reader;
  
  public HandlePrimitive(T defVal, Class<T> boxedClass, @Nullable Class<?> primitiveClass, WriterFunc<T> writer, ReaderFunc<T> reader) {
    this.defaultValue = defVal;
    this.primitiveClass = primitiveClass;
    this.boxedClass = boxedClass;
    this.writer = writer;
    this.reader = reader;
  }

  @Override
  public @Nullable IHandler<T> getHandler(Registry registry, Type type) {
    Class<?> primitive = primitiveClass;
    return (primitive != null && TypeUtil.isAssignable(primitive, type)) || TypeUtil.isAssignable(boxedClass, type) ? this : null;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name,
      T object) throws IllegalArgumentException, IllegalAccessException {
    writer.set(nbt, name, object);
    return true;
  }

  @Override
  public @Nullable T read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable T object) {
    return nbt.hasKey(name) ? reader.get(nbt, name) : object != null ? object : defaultValue;
  }

}
