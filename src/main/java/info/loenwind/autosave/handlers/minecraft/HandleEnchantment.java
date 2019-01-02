package info.loenwind.autosave.handlers.minecraft;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NBTTagCompound;

public class HandleEnchantment implements IHandler<Enchantment> {

  public HandleEnchantment() {
  }

  @Override
  public Class<?> getRootType() {
    return Enchantment.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, Enchantment object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    nbt.setShort(name, (short) Enchantment.getEnchantmentID(object));
    return true;
  }

  @Override
  public @Nullable Enchantment read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, @Nullable Enchantment object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    if (nbt.hasKey(name)) {
      return Enchantment.getEnchantmentByID(nbt.getShort(name));
    }
    return object;
  }

}
