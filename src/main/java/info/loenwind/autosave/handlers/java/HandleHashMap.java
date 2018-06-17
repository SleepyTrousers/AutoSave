package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.TypeUtil;

@SuppressWarnings("rawtypes")
public class HandleHashMap extends HandleAbstractMap<HashMap> {
  
  public HandleHashMap() throws NoHandlerFoundException {
    this(Registry.GLOBAL_REGISTRY, new Type[0]);
  }

  protected HandleHashMap(Registry registry, Type... types) throws NoHandlerFoundException {
    super(registry, types);
  }

  @Override
  protected HashMap createMap() {
    return new HashMap();
  }

  @Override
  protected IHandler<? extends HashMap> create(Registry registry, Type... types) throws NoHandlerFoundException {
    return new HandleHashMap(registry, types);
  }

  @Override
  protected boolean canHandle(Type type) {
    return TypeUtil.toClass(type) == Map.class || super.canHandle(type);
  }
  
  @Override
  public Class<?> getRootType() {
    return HashMap.class;
  }
}
