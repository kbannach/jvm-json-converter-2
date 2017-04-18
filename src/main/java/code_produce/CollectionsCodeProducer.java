package code_produce;

import java.lang.reflect.Field;

public enum CollectionsCodeProducer {
   ;

   static String processArrayType(Field f, Class< ? > fType) {
      StringBuilder ret = new StringBuilder(StringWrapper.fieldNameWithColon(f.getName()) + "[ \" +");
      // TODO Auto-generated method stub
      return ret.append("+ \" ]").toString();
   }

   static String processListType(Field f, Class< ? > fType) {
      // TODO Auto-generated method stub
      return null;
   }

   static String processMapType(Field f, Class< ? > fType) {
      // TODO Auto-generated method stub
      return null;
   }

}
