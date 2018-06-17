package info.loenwind.autosave.handlers.java;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.nbt.NBTTagCompound;

public class HandleFloatArray implements IHandler<float[]> {

  public HandleFloatArray() {
  }

  @Override
  public Class<?> getRootType() {
    return float[].class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, String name,
      float[] object) throws IllegalArgumentException, IllegalAccessException {
    int len = 0;
    for (int i = object.length; i > 0; i--) {
      if (object[i - 1] != 0) {
        len = i;
        break;
      }
    }
    int[] tmp = new int[len];
    for (int i = 0; i < len; i++) {
      tmp[i] = Float.floatToIntBits(object[i]);
    }
    nbt.setIntArray(name, tmp);
    return true;
  }

  @Override
  public @Nullable float[] read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, @Nullable Field field, String name,
      @Nullable float[] object) {
    if (nbt.hasKey(name)) {
      int[] tmp = nbt.getIntArray(name);
      if (object == null) {
        object = new float[tmp.length];
      }
      for (int i = 0; i < object.length; i++) {
        if (i < tmp.length) {
          object[i] = Float.intBitsToFloat(tmp[i]);
        } else {
          object[i] = 0;
        }
      }
    }
    return object;
  }

}
