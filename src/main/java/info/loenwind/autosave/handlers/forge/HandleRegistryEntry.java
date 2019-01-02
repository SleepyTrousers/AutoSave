package info.loenwind.autosave.handlers.forge;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import info.loenwind.autosave.util.NonnullType;
import info.loenwind.autosave.util.NullHelper;
import info.loenwind.autosave.util.TypeUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class HandleRegistryEntry implements IHandler<IForgeRegistryEntry> {
  
  private final Map<Class<?>, IForgeRegistry> registryCache = new HashMap<>();
  
  @Override
  public Class<?> getRootType() {
    return IForgeRegistryEntry.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, IForgeRegistryEntry object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    ResourceLocation loc = object.getRegistryName();
    if (loc == null) {
      throw new IllegalArgumentException("Registry entry must be registered to be stored: " + object);
    }
    nbt.setString(name, loc.toString());
    return true;
  }

  @Override
  @Nullable
  public IForgeRegistryEntry read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, @Nullable IForgeRegistryEntry object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    IForgeRegistry reg = findRegistry(object == null ? type : NullHelper.notnullF(object.getRegistryType(), "IForgeRegistryEntry#getRegistryType"));
    ResourceLocation rl = new ResourceLocation(nbt.getString(name));
    IForgeRegistryEntry ret = reg.getValue(rl);
    return ret;
  }
  
  @SuppressWarnings("unchecked")
  private IForgeRegistry findRegistry(Type type) {
    Class<?> cls = TypeUtil.toClass(type);
    return NullHelper.notnullJ(registryCache.computeIfAbsent(cls, $ -> {
      // Otherwise walk the entire class hierarchy and find IFRE/IFRE.Impl, then extract the type parameter
      Iterator<@NonnullType Type> iter = walkHierarchy(type);
      while (iter.hasNext()) {
        Type t = iter.next();
        Class<?> c = TypeUtil.toClass(t);
        IForgeRegistry reg = GameRegistry.findRegistry((Class) c);
        if (reg != null) {
          return reg;
        }
      }
      throw new IllegalArgumentException("Not a registry object subtype");
    }), "Map#computeIfAbsent");
  }
  
  private Iterator<Type> walkHierarchy(Type cls) {
    return new Iterator<Type>() {

      Set<Class<?>>                 seenInterfaces = new HashSet<>();
      ArrayDeque<@NonnullType Type> queue          = new ArrayDeque<>();
      {
        queue.add(cls);
      }

      @Override
      public boolean hasNext() {
        return !queue.isEmpty();
      }

      @Override
      public Type next() {
        // BFS treating class hierarchy as a tree, duplicate interfaces are skipped
        if (hasNext()) {
          Type ret = queue.pop();
          Class<?> cls = TypeUtil.toClass(ret);
          if (cls != Object.class && !cls.isInterface()) {
            queue.push(NullHelper.notnullJ(cls.getGenericSuperclass(), "Class#getGenericSuperclass"));
            for (Type itf : cls.getGenericInterfaces()) {
              if (seenInterfaces.add(TypeUtil.toClass(NullHelper.notnullJ(itf, "Class#getGenericInterfaces")))) {
                queue.push(NullHelper.notnullJ(itf, "Class#getGenericInterfaces"));
              }
            }
          }
          return ret;
        }
        throw new NoSuchElementException();
      }
    };
  }

}
