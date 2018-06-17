package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.TypeUtil;

@SuppressWarnings("rawtypes")
public class HandleArrayList extends HandleAbstractCollection<ArrayList> {
  
  public HandleArrayList() throws NoHandlerFoundException {
    this(new Type[0]);
  }

  protected HandleArrayList(Type... types) throws NoHandlerFoundException {
    super(types);
  }

  @Override
  protected ArrayList makeCollection() {
    return new ArrayList();
  }
  
  @Override
  protected boolean canHandle(Type type) {
    return TypeUtil.toClass(type) == List.class || super.canHandle(type);
  }
  
  @Override
  public Class<?> getRootType() {
    return ArrayList.class;
  }

  @Override
  protected IHandler<? extends ArrayList> create(Type... types) throws NoHandlerFoundException {
    return new HandleArrayList(types);
  }

}
