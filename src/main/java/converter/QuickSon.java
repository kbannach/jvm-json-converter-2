package converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import code_produce.CodeProducer;

public enum QuickSon {
   SINGLETON;

   private ClassPool                       pool;
   private Map<Class< ? >, IJsonConverter> cache;

   private QuickSon() {
      this.pool = ClassPool.getDefault();
      this.cache = new ConcurrentHashMap<>();
   }

   public String toJson(Object o) {
      if (o == null) {
         return "null";
      } else {
         try {
            if (!this.cache.containsKey(o.getClass())) {
               this.cache.put(o.getClass(), getConverter(o.getClass()));
            }
            return this.cache.get(o.getClass()).toJson(o);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
   }

   private IJsonConverter getConverter(Class< ? > cls) throws CannotCompileException, InstantiationException, IllegalAccessException, NotFoundException {
      // new class with a random name, as this name is not needed in any way
      CtClass converterClass = this.pool.makeClass(UUID.randomUUID().toString());
      converterClass.addMethod(CtNewMethod.make(getConverterMethodBody(cls), converterClass));

      // ensuring interface compatibility
      converterClass.addMethod(CtNewMethod.make("public String toJson(Object o){return toJson((" + cls.getName() + ")o);}", converterClass));
      converterClass.setInterfaces(new CtClass[]{this.pool.get(IJsonConverter.class.getName())});

      IJsonConverter result = (IJsonConverter) this.pool.toClass(converterClass).newInstance();
      // this allows us to save memory
      converterClass.detach();
      return result;
   }

   // actual JSON producing code is written here!
   private String getConverterMethodBody(Class< ? > cls) {
      List<String> fieldsStrings = new ArrayList<>();
      Set<Field> fields = getClassFields(cls);
      for (Field f : fields) {
         String s = CodeProducer.produceFieldString(f);
         System.out.println(s);
         fieldsStrings.add(CodeProducer.produceFieldString(f));
      }

      StringBuilder sb = new StringBuilder("public String toJson(" + cls.getName() + " o) { return \"{ \"+");
      sb.append(Arrays.stream(fieldsStrings.toArray(new String[]{})).collect(Collectors.joining("+\", \"+")));
      sb.append("+\" }\"; }");
      return sb.toString();
   }

   private Set<Field> getClassFields(Class< ? > cls) {
      Set<Field> ret = new HashSet<>();
      Arrays.stream(cls.getFields()).forEach(f -> ret.add(f));
      Arrays.stream(cls.getDeclaredFields()).forEach(f -> ret.add(f));
      return ret;
   }

   public String arrayToJson(Object[] arr) {
      return "[ " + Arrays.stream(arr).map(obj -> QuickSon.SINGLETON.toJson(obj)).collect(Collectors.joining(", ")) + " ]";
   }

   public String listToJson(List< ? > list) {
      return this.arrayToJson(list.toArray());
   }

   public String mapToJson(Map< ? , ? > map) {
      // TODO
      return null;
   }

}
