package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.engine.StorableEngine;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.NonnullType;
import info.loenwind.autosave.util.NullHelper;
import info.loenwind.autosave.util.TypeUtil;
import net.minecraft.nbt.NBTTagCompound;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HandleEnumMap<K extends Enum<K>> extends HandleAbstractMap<EnumMap<K, ?>>{

  private final Class<K> enumClass;
  private final K[] enumValues;
  
  public HandleEnumMap() throws NoHandlerFoundException {
    super(Registry.GLOBAL_REGISTRY, new Type[0]);
    this.enumClass = (Class<K>) (Class) Enum.class;
    this.enumValues = (K[]) new Enum[0];
  }
  
  protected HandleEnumMap(Registry registry, Class<K> enumClass, Class<?> valueClass) throws NoHandlerFoundException {
    super(registry, enumClass, valueClass);
    this.enumClass = enumClass;
    this.enumValues = NullHelper.notnullJ(enumClass.getEnumConstants(), "Class#getEnumConstants");
  }

  @Override
  public Class<?> getRootType() {
    return EnumMap.class;
  }
  
  @Override
  protected IHandler<? extends EnumMap<K, ?>> create(Registry registry, @NonnullType Type... types) throws NoHandlerFoundException {
    return new HandleEnumMap<K>(registry, (Class<K>) TypeUtil.toClass(types[0]), TypeUtil.toClass(types[1]));
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name,
      EnumMap<K, ?> object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    NBTTagCompound tag = new NBTTagCompound();
    for (K key : enumValues) {
      Object val = object.get(key);
      String keystr = NullHelper.notnullJ(Integer.toString(key.ordinal()), "Integer.toString is null");
      if (val != null) {
        storeRecursive(1, registry, phase, tag, keystr, val);
      } else {
        tag.setBoolean(keystr + StorableEngine.NULL_POSTFIX, true);
      }
    }
    nbt.setTag(name, tag);
    return true;
  }

  @Override
  public @Nullable EnumMap<K, ?> read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field,
      String name, @Nullable EnumMap<K, ?> object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      if (object == null) {
        object = createMap();
      }
      NBTTagCompound tag = nbt.getCompoundTag(name);
      for (K key : enumValues) {
        String keystr = NullHelper.notnullJ(Integer.toString(key.ordinal()), "Integer.toString is null");
        if (!tag.getBoolean(keystr + StorableEngine.NULL_POSTFIX)) {
          object.put(key, readRecursive(1, registry, phase, tag, null, keystr, null));
        }
      }
    }
    return object;
  }
  
  @Override
  protected EnumMap<K, ?> createMap() {
    return new EnumMap<>(enumClass);
  }

}
