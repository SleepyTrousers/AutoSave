package info.loenwind.autosave.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeUtil {

  /**
   * Convert a {@link Type} to a {@link Class}, discarding all generic
   * information.
   * 
   * @param type
   *          The Type to convert.
   * @return The raw Class object.
   */
  public static Class<?> toClass(Type type) {
    return (Class<?>) (type instanceof ParameterizedType ? NullHelper.notnullJ(((ParameterizedType) type).getRawType(), "ParameterizedType#getRawType") : type);
  }

  /**
   * Analogous to {@link Class#isAssignableFrom(Class)}, but converts the passed
   * Type to a class via {{@link #toClass(Type)} first.
   * 
   * @param clazz
   *          The class to be the left-hand comparison
   * @param type
   *          The type to be the right-hand comparison
   * @return {@link Class#isAssignableFrom(Class)}
   */
  public static boolean isAssignable(Class<?> clazz, Type type) {
    return clazz.isAssignableFrom(toClass(type));
  }

}
