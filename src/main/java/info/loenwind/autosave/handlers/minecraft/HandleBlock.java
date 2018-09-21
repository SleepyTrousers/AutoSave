package info.loenwind.autosave.handlers.minecraft;

import java.lang.reflect.Type;
import java.util.Set;

import javax.annotation.Nullable;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import info.loenwind.autosave.util.NBTAction;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class HandleBlock implements IHandler<Block> {

  @Override
  public Class<?> getRootType() {
    return Block.class;
  }

  @Override
  public boolean store(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name, Block object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    ResourceLocation loc = object.getRegistryName();
    if (loc == null) {
      throw new IllegalArgumentException("Block must be registered to be saved.");
    }
    nbt.setString(name, loc.toString());
    return true;
  }

  @Override
  public @Nullable Block read(Registry registry, Set<NBTAction> phase, NBTTagCompound nbt, Type type, String name,
      @Nullable Block object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString(name)));
  }
}
