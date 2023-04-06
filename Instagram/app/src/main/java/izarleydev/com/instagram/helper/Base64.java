package izarleydev.com.instagram.helper;

public class Base64 {
    public static String codBase64(String texto) {
        return android.util.Base64.encodeToString(texto.getBytes(), android.util.Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }
    public static String decBase64 (String textoCod){
        return new String(android.util.Base64.decode(textoCod, android.util.Base64.DEFAULT));
    }

}
