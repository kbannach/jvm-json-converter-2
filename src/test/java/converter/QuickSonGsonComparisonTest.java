package converter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojoSamples.NestedObject;
import pojoSamples.Student;
import pojoSamples.StudentWithCollections;
import pojoSamples.StudentWithGetters;
import com.google.gson.Gson;

public class QuickSonGsonComparisonTest {

   private Map<Class< ? >, IJsonConverter> initialCache;

   @SuppressWarnings("unchecked")
   @Before
   public void saveInitialQuickSonCache() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
      Field cacheField = QuickSon.class.getDeclaredField("cache");
      cacheField.setAccessible(true);
      this.initialCache = (Map<Class< ? >, IJsonConverter>) cacheField.get(QuickSon.SINGLETON);
   }

   @After
   public void restoreInitialQuickSonCache() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
      Field cacheField = QuickSon.class.getDeclaredField("cache");
      cacheField.setAccessible(true);
      cacheField.set(QuickSon.SINGLETON, this.initialCache);
   }

   @Test
   public void comparisonOfSimpleObj() {
      Gson g = new Gson();

      Student s1 = new Student();
      s1.age = 20;
      s1.name = "Jason Tester";

      compare(s1, QuickSon.SINGLETON::toJson, g::toJson);
   }

   @Test
   public void comparisonOfSimpleObjWithGetters() {
      Gson g = new Gson();

      StudentWithGetters s2 = new StudentWithGetters();
      s2.setAge(21);
      s2.setName("Jason Tester II");

      compare(s2, QuickSon.SINGLETON::toJson, g::toJson);
   }

   @Test
   public void comparisonOfNestedObj() {
      Gson g = new Gson();

      NestedObject obj1 = new NestedObject();
      NestedObject obj2 = new NestedObject();
      NestedObject obj3 = new NestedObject();
      obj1.id = 1;
      obj2.id = 2;
      obj3.id = 3;
      obj1.child = obj2;
      obj2.child = obj3;

      compare(obj1, QuickSon.SINGLETON::toJson, g::toJson);
   }

   @Test
   public void comparisonOfObjWithCollections() {
      Gson g = new Gson();
      Integer[] ints = new Integer[]{1, 2, 3, 4, 5};
      List<String> strgs = Stream.of("a", "b", "c", "d").collect(Collectors.toList());

      StudentWithCollections s = new StudentWithCollections();
      s.id = 1;
      s.integers = ints;
      s.strings = strgs;

      compare(s, QuickSon.SINGLETON::toJson, g::toJson);
   }

   private static void compare(Object o, Function<Object, String> quickSonFun, Function<Object, String> gSonFun) {
      long qTime = measure(quickSonFun, o);
      long gTime = measure(gSonFun, o);
      System.out.println(o.getClass().getSimpleName() + " object: QuickSon = " + qTime + " Gson = " + gTime + " ratio = " + (double) gTime / qTime + " (real time)");
      qTime = measure(quickSonFun, o);
      gTime = measure(gSonFun, o);
      System.out.println(o.getClass().getSimpleName() + " object: QuickSon = " + qTime + " Gson = " + gTime + " ratio = " + (double) gTime / qTime + " (time without QuickSon converters generation)");
   }

   private static long measure(Function<Object, String> converter, Object o) {
      long t1 = System.currentTimeMillis();
      converter.apply(o);

      for (int i = 0; i < 500000; i++) {
         converter.apply(o);
      }
      return System.currentTimeMillis() - t1;
   }
}
