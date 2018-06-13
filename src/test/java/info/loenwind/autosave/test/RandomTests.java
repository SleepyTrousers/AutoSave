package info.loenwind.autosave.test;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.util.Log;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RandomTests {
  
  private static class StringHolder {
    
    @Store
    public String foo = "Hello World!";
    
    @Store
    public ItemStack stack = null;
    
  }
  
  @BeforeAll
  public static void setup() {
    Log.enableExtremelyDetailedNBTActivity("AutoStoreTests", true);
    Bootstrap.register();
  }
  
  @Test
  public void randomTests() {
    
    StringHolder test = new StringHolder(), res = new StringHolder();
    test.foo = "I am saved!";
    test.stack = new ItemStack(Items.APPLE);
    
    NBTTagCompound tag = new NBTTagCompound();
    Writer.write(tag, test);
    Reader.read(tag, res);
    
    Assert.assertEquals(test.foo, res.foo);
    Assert.assertNotNull(res.stack);
    Assert.assertFalse(res.stack.isEmpty());
    Assert.assertEquals(test.stack.getItem(), res.stack.getItem());
  }

}
