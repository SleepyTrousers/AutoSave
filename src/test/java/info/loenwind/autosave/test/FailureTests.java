package info.loenwind.autosave.test;

import java.util.BitSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import net.minecraft.nbt.NBTTagCompound;

public class FailureTests {
  
  private class BadClass {
    
    private @Store BitSet invalid = new BitSet();
    
  }
  
  @Test
  public void testNoHandler() {
    try {
      Writer.write(new NBTTagCompound(), new BadClass());
    } catch (RuntimeException e) {
      if (e.getCause() instanceof NoHandlerFoundException) {
        return;
      }
    }
    Assertions.fail("BadClass saved successfully.");
  }

}
