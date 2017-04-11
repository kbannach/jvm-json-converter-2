package converter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import pojoSamples.Student;

public class QuickSonTest {

   @Test
   public void studentPojoTest() {
      Student s = new Student();
      s.age = 20;
      s.name = "test";
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"name\": \"test\", \"age\": 20 }");
   }

   @Test
   public void studentPojoWithNullNameTest() {
      Student s = new Student();
      s.age = 21;
      assertThat(QuickSon.SINGLETON.toJson(s)).isEqualToIgnoringWhitespace("{ \"name\": null, \"age\": 21 }");
   }
}
