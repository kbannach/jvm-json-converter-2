package converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import pojoSamples.NestedStudent;
import pojoSamples.Student;

public class QuickSonTest {

   @Test
   public void studentTest() {
      Student s = new Student();
      s.age = 20;
      s.name = "test";
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"name\": \"test\", \"age\": 20 }");
   }

   @Test
   public void studentWithNullNameTest() {
      Student s = new Student();
      s.age = 21;
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"name\": null, \"age\": 21 }");
   }

   @Test
   public void nestedStudentsTest() {
      // prepare
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
            "\"student\": { \"name\": \"first\", \"age\": 21 }, " + //
            "\"child\": { " + //
            "\"student\": { \"name\": \"second\", \"age\": 22 }, " + //
            "\"child\": null" + //
            " } " + //
            "}";
      // test
      assertThat(QuickSon.SINGLETON.toJson(n1)).isEqualToIgnoringWhitespace(expected);
   }
}
