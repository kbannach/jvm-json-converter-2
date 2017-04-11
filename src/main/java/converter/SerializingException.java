package converter;

@SuppressWarnings("serial")
public class SerializingException extends Throwable {

   public SerializingException(String message, Throwable cause) {
      super(message, cause);
   }

   public SerializingException(String message) {
      super(message);
   }

   public SerializingException(Throwable cause) {
      super(cause);
   }

}
