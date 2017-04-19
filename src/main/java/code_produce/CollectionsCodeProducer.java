package code_produce;

import java.lang.reflect.Field;
import converter.QuickSon;

public enum CollectionsCodeProducer {
   ;

   private enum COLLECTION_TYPE {
      ARRAY("array"), LIST("list"), MAP("map");

      private COLLECTION_TYPE(String string) {
         this.string = string;
      }

      private final String string;

      public String getString() {
         return this.string;
      }
   }

   static String processArrayType(Field f) {
      return convert(COLLECTION_TYPE.ARRAY, f);
   }

   static String processListType(Field f) {
      return convert(COLLECTION_TYPE.LIST, f);
   }

   static String processMapType(Field f) {
      return convert(COLLECTION_TYPE.MAP, f);
   }

   private static String convert(COLLECTION_TYPE colType, Field f) {
      String ret = StringWrapper.fieldNameWithColon(f.getName()) + "\" +";
      ret += QuickSon.class.getName() + ".SINGLETON." + colType.getString() + "ToJson(o." + f.getName() + ") ";
      return ret;
   }

}
