package code_produce;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import converter.QuickSon;

public enum CodeProducer {
   ;

   private static Set<Class< ? >> AUTOBOXED_PRIMITIVES = getAutoboxedPrimitives();

   private static Set<Class< ? >> getAutoboxedPrimitives() {
      Set<Class< ? >> ret = new HashSet<>();
      ret.add(Boolean.class);
      ret.add(Character.class);
      ret.add(Byte.class);
      ret.add(Short.class);
      ret.add(Integer.class);
      ret.add(Long.class);
      ret.add(Float.class);
      ret.add(Double.class);
      return ret;
   }

   /**
    * produces json constructing code based on field type
    */
   public static String produceFieldString(Field f) {
      Class< ? > fType = f.getType();
      if (fType.isPrimitive() || AUTOBOXED_PRIMITIVES.contains(fType)) {
         return StringWrapper.fieldNameWithColon(f.getName()) + "\" + " + StringWrapper.getFieldAccessorName(f);
      } else if (fType.equals(String.class)) {
         return StringWrapper.fieldNameWithColon(f.getName()) + StringWrapper.stringValueWithNullCheck(f);
      } else if (fType.isArray()) {
         return CollectionsCodeProducer.processArrayType(f);
      } else if (fType.isAssignableFrom(List.class)) {
         return CollectionsCodeProducer.processListType(f);
      } else if (fType.isAssignableFrom(Map.class)) {
         return CollectionsCodeProducer.processMapType(f);
      } else {
         return StringWrapper.fieldNameWithColon(f.getName()) + "\" + " + QuickSon.class.getName() + ".SINGLETON.toJson(o." + f.getName() + ") ";
      }
   }

}
