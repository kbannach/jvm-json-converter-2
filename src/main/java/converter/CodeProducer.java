package converter;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
   // TODO only public fields supported for now (no getter/setter support)
   public static String produceFieldString(Field f) {
      Class< ? > fType = f.getType();
      if (fType.isPrimitive() || AUTOBOXED_PRIMITIVES.contains(fType)) {
         return fieldNameWithColon(f.getName()) + "\"+o." + f.getName();
      } else if (fType.equals(String.class)) {
         return fieldNameWithColon(f.getName()) + stringValueWithNullCheck(f.getName());
      } else if (fType.isArray()) {
         return processArrayType(f, fType);
      } else if (fType.isAssignableFrom(List.class)) {
         return processListType(f, fType);
      } else if (fType.isAssignableFrom(Map.class)) {
         return processMapType(f, fType);
      } else {
         return fieldNameWithColon(f.getName()) + "\" + " + QuickSon.class.getName() + ".SINGLETON.toJson(o." + f.getName() + ") ";
      }
   }

   private static String stringValueWithNullCheck(String fName) {
      String ret = "\" + ( o." + fName + " == null ? \"null\" : \"\\\"\"+o." + fName + "+\"\\\"\" )";
      return ret;
   }

   private static String fieldNameWithColon(String fName) {
      return "\"\\\"" + fName + "\\\": ";
   }

   private static String processArrayType(Field f, Class< ? > fType) {
      // TODO Auto-generated method stub
      return null;
   }

   private static String processListType(Field f, Class< ? > fType) {
      // TODO Auto-generated method stub
      return null;
   }

   private static String processMapType(Field f, Class< ? > fType) {
      // TODO Auto-generated method stub
      return null;
   }

}
