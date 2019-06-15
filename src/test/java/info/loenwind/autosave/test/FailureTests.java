package info.loenwind.autosave.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import info.loenwind.autosave.Reader;
import info.loenwind.autosave.Writer;
import info.loenwind.autosave.annotations.AfterRead;
import info.loenwind.autosave.annotations.Factory;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import net.minecraft.nbt.CompoundNBT;

public class FailureTests {
  
  private class Unknown {}
  
  private class BadField {
    
    private @Store Unknown invalid = new Unknown();
    
  }
  
  private class BadGeneric {
    
    private @Store List<Unknown> invalid = new ArrayList<>();
    
  }
  
  private class RawTypes {
    
    @SuppressWarnings("rawtypes")
    private @Store List rawtyped = new ArrayList<>();
    
  }
  
  @Storable
  private static class DoubleFactory {
    
    @Factory
    public static DoubleFactory factoryA() { return new DoubleFactory(); }
    
    @Factory
    public static DoubleFactory factoryB() { return new DoubleFactory(); }
  }
  
  @Storable
  private static class DoubleFactoryCtor {
    
    @Factory public static DoubleFactoryCtor factory() { return new DoubleFactoryCtor(); }
    
    @Factory DoubleFactoryCtor() {}
    
  }
  
  private static class InvalidFactoryParams {
    
    @Factory public static InvalidFactoryParams factory(String parameter) { return new InvalidFactoryParams(); }
    
  }
  
  private static class InvalidFactoryReturn {
    
    @Factory public static Object factory() { return new InvalidFactoryReturn(); }
    
  }
    
  private void testFails(@Nonnull Object toWrite) {
    try {
      Writer.write(new CompoundNBT(), toWrite);
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

  @Test
  public void testMissingFactory() {
    @Storable
    class NoFactory { 
      public NoFactory(String unused) {}
    }
    class Holder {
      @Store
      private NoFactory noFactory;
    }
    
    CompoundNBT tag = new CompoundNBT();
    Holder toWrite = new Holder();
    toWrite.noFactory = new NoFactory("foo");
    Writer.write(tag, toWrite);
    Assertions.assertThrows(IllegalArgumentException.class, () -> Reader.read(tag, new Holder()));
  }
  
  @Test
  public void testDoubleFactory() {    
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new DoubleFactory()));
  }
  
  @Test
  public void testDoubleFactoryCtor() {    
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new DoubleFactoryCtor()));
  }
  
  @Test
  public void testInvalidFactoryParams() {    
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new InvalidFactoryParams()));
  }
  
  @Test
  public void testInvalidFactoryCtorParams() {
    class InvalidFactoryCtorParams {
      @Factory InvalidFactoryCtorParams(String parameter) {}
    }
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new InvalidFactoryCtorParams("foo")));
  }
  
  @Test
  public void testInvalidFactoryReturn() {    
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new InvalidFactoryReturn()));
  }
  
  @Test
  public void testInvalidAfterReadParams() {
    class InvalidAfterReadParams {
      @AfterRead public void onRead(String unused) {}
    }
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new InvalidAfterReadParams()));
  }
  
  @Test
  public void testInvalidAfterReadReturn() {
    class InvalidAfterReadReturn {
      @AfterRead public InvalidAfterReadReturn onRead() { return this; }
    }
    Assertions.assertThrows(IllegalArgumentException.class, () -> Writer.write(new CompoundNBT(), new InvalidAfterReadReturn()));
  }
}
