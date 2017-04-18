package code_produce;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public enum StringWrapper {
   ;

   static String stringValueWithNullCheck(Field f) {
      return "\" + ( " + getFieldAccessorName(f) + " == null ? \"null\" : \"\\\"\"+" + getFieldAccessorName(f) + "+\"\\\"\" )";
   }

   static String getFieldAccessorName(Field f) {
      Method getter = getGetter(f);
      if (getter != null) {
         return "o." + getter.getName() + "()";
      } else {
         return "o." + f.getName();
      }
   }

   static String fieldNameWithColon(String fName) {
      return "\"\\\"" + fName + "\\\": ";
   }

   // Source: http://stackoverflow.com/a/2638662/2757140
   static Method getGetter(Field field) {
      try {
         Class< ? > type = field.getDeclaringClass();
         return new PropertyDescriptor(field.getName(), type).getReadMethod();
      } catch (IntrospectionException e) {
         return null;
      }
   }

}
