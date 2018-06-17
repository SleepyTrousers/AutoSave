package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.TypeUtil;

@SuppressWarnings("rawtypes")
public class HandleHashMap extends HandleAbstractMap<HashMap> {
  
  public HandleHashMap() throws NoHandlerFoundException {
    this(new Type[0]);
  }

  protected HandleHashMap(Type... types) throws NoHandlerFoundException {
    super(types);
  }

  @Override
  protected HashMap createMap() {
    return new HashMap();
  }

  @Override
  protected IHandler<? extends HashMap> create(Type... types) throws NoHandlerFoundException {
    return new HandleHashMap(types);
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
