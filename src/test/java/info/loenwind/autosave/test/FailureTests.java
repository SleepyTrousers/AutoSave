package info.loenwind.autosave.test;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import net.minecraft.nbt.NBTTagCompound;

public class FailureTests {
  
  private class BadField {
    
    private @Store BitSet invalid = new BitSet();
    
  }
  
  private class BadGeneric {
    
    private @Store List<BitSet> invalid = new ArrayList<>();
    
  }
  
  private class RawTypes {
    
    @SuppressWarnings("rawtypes")
    private @Store List rawtyped = new ArrayList<>();
    
  }
  
  private void testFails(@Nonnull Object toWrite) {
    try {
      Writer.write(new NBTTagCompound(), toWrite);
    } catch (RuntimeException e) {
      if (e.getCause() instanceof NoHandlerFoundException) {
        return;
      }
    }
    Assertions.fail(toWrite.getClass().getSimpleName() + " saved successfully.");
  }
  
  @Test
  public void testNoHandler() {
    testFails(new BadField());
  }
  
  @Test
  public void testNoGenericHandler() {
    testFails(new BadGeneric());
  }
  
  @Test
  public void testRawType() {
    testFails(new RawTypes());
  }

}
