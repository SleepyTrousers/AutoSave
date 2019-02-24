package info.loenwind.autosave.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

public class VersionProxy<T> {

  enum GameVersion {
    BEFORE_1_13,
    V1_13_x,
    ;

    static GameVersion discover() {
      try {
        Class.forName("net.minecraftforge.fml.common.FMLCommonHandler");
        return GameVersion.BEFORE_1_13;
      } catch (ClassNotFoundException e) {
        return GameVersion.V1_13_x;
      }
    }
  }

  private static final GameVersion VERSION = GameVersion.discover();
  
  private final @Nonnull T func;
  
  VersionProxy(T func) {
    this.func = func;
  }

  public T getFunction() {
    return func;
  }
  
  private static final Lookup LOOKUP = MethodHandles.publicLookup();

  /*
   * In 1.13, NBTUtil.writeBlockState lost the NBTTagCompound param, instead
   * returning a new tag. This proxy unifies the two APIs, preferring the style
   * of the new one (returning a new tag instead of consuming one).
   */

  private static final MethodHandle NBTUtil$writeBlockState;
  static {
    try {
      switch (VERSION) {
      default:
      case BEFORE_1_13:
        NBTUtil$writeBlockState = null;
        break;
      case V1_13_x:
        NBTUtil$writeBlockState = LOOKUP.findStatic(NBTUtil.class, "func_190009_a", MethodType.methodType(NBTTagCompound.class, IBlockState.class));
        break;
      }
    } catch (IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static final VersionProxy<Function<@NonnullType IBlockState, @NonnullType NBTTagCompound>> WRITE_BLOCKSTATE = new VersionProxy<>(s -> {
    if (NBTUtil$writeBlockState != null) {
      try {
        return (NBTTagCompound) NBTUtil$writeBlockState.invokeExact(s);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
    NBTTagCompound tag = new NBTTagCompound();
    NBTUtil.writeBlockState(tag, s);
    return tag;
  });
  
  /*
   * In 1.13, the ItemStack(NBTTagCompound) constructor was removed in favor of
   * a static factory method, read(NBTTagCompound).
   */

  private static final MethodHandle ItemStack$read;
  static {
    try {
      switch (VERSION) {
      default:
      case BEFORE_1_13:
        ItemStack$read = null;
        break;
      case V1_13_x:
        ItemStack$read = LOOKUP.findStatic(ItemStack.class, "func_199557_a", MethodType.methodType(ItemStack.class, NBTTagCompound.class));
        break;
      }
    } catch (IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public static final VersionProxy<Function<@NonnullType NBTTagCompound, @NonnullType ItemStack>> READ_ITEMSTACK = new VersionProxy<>(t -> {
    if (ItemStack$read != null) {
      try {
        return (ItemStack) ItemStack$read.invokeExact(t);
      } catch (Throwable e) {
        throw new RuntimeException(e);
      }
    }
    return new ItemStack(t);
  });
}
