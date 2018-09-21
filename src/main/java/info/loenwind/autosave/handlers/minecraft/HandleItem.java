package info.loenwind.autosave.handlers.minecraft;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public class HandleItem implements IHandler<Item> {

  public HandleItem() {
  }

  @Override
  public Class<?> getRootType() {
    return Item.class;
  }

  // Note: We can use the item ID here because ItemStack also uses it in its NBT-methods. So if the ID breaks, ItemStacks break even harder than we do here. 

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, Item object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    nbt.setShort(name, (short) Item.getIdFromItem(object));
    return true;
  }

  @Override
  public @Nullable Item read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name,
      @Nullable Item object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      return Item.getItemById(nbt.getShort(name));
    }
    return object;
  }

}
