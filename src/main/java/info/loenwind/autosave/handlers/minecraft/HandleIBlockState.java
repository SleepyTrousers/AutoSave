package info.loenwind.autosave.handlers.minecraft;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public class HandleIBlockState implements IHandler<IBlockState> {

  public HandleIBlockState() {
  }

  @Override
  public Class<?> getRootType() {
    return IBlockState.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, IBlockState object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    NBTTagCompound tag = new NBTTagCompound();
    NBTUtil.writeBlockState(tag, object);
    nbt.setTag(name, tag);
    return true;
  }

  @Override
  public @Nullable IBlockState read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name,
      @Nullable IBlockState object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return NBTUtil.readBlockState(nbt.getCompoundTag(name));
  }

}
