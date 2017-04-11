package converter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public enum QuickSon {
   SINGLETON;

   private ClassPool                       pool;
   private Map<Class< ? >, IJsonConverter> cache;

   private QuickSon() {
      this.pool = ClassPool.getDefault();
      this.cache = new HashMap<>();
   }

   public String toJson(Object o) {
      if (o == null) {
         return "null";
      } else {
         try {
            // TODO: this is not thread safe!
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
      for (Field f : cls.getFields()) {
         fieldsStrings.add(CodeProducer.produceFieldString(f));
      }

      StringBuilder sb = new StringBuilder("public String toJson(" + cls.getName() + " o) { return \"{ \"+");
      sb.append(Arrays.stream(fieldsStrings.toArray(new String[]{})).collect(Collectors.joining("+\", \"+")));
      sb.append("+\" }\"; }");
      return sb.toString();
   }

}
