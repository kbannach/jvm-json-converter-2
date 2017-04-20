package converter;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import pojoSamples.NestedObject;
import pojoSamples.NestedStudent;
import pojoSamples.Student;
import pojoSamples.StudentWithCollections;
import pojoSamples.StudentWithGetters;

// TODO compare with GSON

public class QuickSonTest {

   @Test
   public void studentTest() {
      Student s = new Student();
      s.age = 20;
      s.name = "test";
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"age\": 20, \"name\": \"test\" }");
   }

   @Test
   public void studentWithNullNameTest() {
      Student s = new Student();
      s.age = 21;
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"age\": 21, \"name\": null }");
   }

   @Test
   public void nestedStudentsTest() {
      NestedStudent n1 = new NestedStudent();
      Student s1 = new Student();
      s1.name = "first";
      s1.age = 21;
      n1.student = s1;

      NestedStudent n2 = new NestedStudent();
      Student s2 = new Student();
      s2.name = "second";
      s2.age = 22;
      n2.student = s2;
      n1.child = n2;

      String expected = "{ " + //
            "\"student\": { \"age\": 21, \"name\": \"first\" }, " + //
            "\"child\": { " + //
            "\"student\": { \"age\": 22, \"name\": \"second\" }, " + //
            "\"child\": null" + //
            " } " + //
            "}";
      // test
      assertThat(QuickSon.SINGLETON.toJson(n1)).isEqualToIgnoringWhitespace(expected);
   }

   @Test
   public void studentWithGettersTest() {
      StudentWithGetters s = new StudentWithGetters();
      s.setAge(20);
      s.setName("test");
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"name\": \"test\", \"age\": 20 }");
   }

   @Test
   public void studentWithCollectionsTest() {
      Integer[] ints = new Integer[]{1, 2, 3, 4, 5};
      List<String> strgs = Stream.of("a", "b", "c", "d").collect(Collectors.toList());

      StudentWithCollections s = new StudentWithCollections();
      s.id = 1;
      s.integers = ints;
      s.strings = strgs;

      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace(//
            "{ \"strings\": [ \"a\", \"b\", \"c\", \"d\" ], " + //
                  "\"id\": 1, " + //
                  "\"integers\": [ 1, 2, 3, 4, 5 ] }" //
      );
   }

   @Test
   public void studentWithCollectionsTest2() {
      Integer[] ints = new Integer[]{1, 2, 3, null, 5};
      List<String> strgs = Stream.of("a", null, "c", "d").collect(Collectors.toList());

      StudentWithCollections s = new StudentWithCollections();
      s.id = 1;
      s.integers = ints;
      s.strings = strgs;

      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace(//
            "{ \"strings\": [ \"a\", null, \"c\", \"d\" ], " + //
                  "\"id\": 1, " + //
                  "\"integers\": [ 1, 2, 3, null, 5 ] }");
   }

   @Test
   public void nestedObjectTest() {
      NestedObject obj1 = new NestedObject();
      NestedObject obj2 = new NestedObject();
      NestedObject obj3 = new NestedObject();
      obj1.id = 1;
      obj2.id = 2;
      obj3.id = 3;
      obj1.child = obj2;
      obj2.child = obj3;

      assertThat(QuickSon.SINGLETON.toJson(obj1)).isEqualToIgnoringWhitespace(//
            "{ \"id\": 1, " + //
                  "\"child\": { \"id\": 2, " + //
                  "\"child\": { \"id\": 3, \"child\": null } " + //
                  "} " + //
                  "}");
   }
}
