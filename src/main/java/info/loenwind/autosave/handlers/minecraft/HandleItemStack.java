package info.loenwind.autosave.handlers.minecraft;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.engine.StorableEngine;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class HandleItemStack implements IHandler<ItemStack> {

  public HandleItemStack() {
  }

  @Override
  public Class<?> getRootType() {
    return ItemStack.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, ItemStack object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (object.isEmpty()) {
      nbt.setBoolean(name + StorableEngine.EMPTY_POSTFIX, true);
    } else {
      NBTTagCompound tag = new NBTTagCompound();
      object.writeToNBT(tag);
      nbt.setTag(name, tag);
    }
    return true;
  }

  @Override
  public @Nullable ItemStack read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name,
      @Nullable ItemStack object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      NBTTagCompound tag = nbt.getCompoundTag(name);
      return new ItemStack(tag);
    } else if (nbt.hasKey(name + StorableEngine.EMPTY_POSTFIX)) {
      return ItemStack.EMPTY;
    }
    return object;
  }
}
